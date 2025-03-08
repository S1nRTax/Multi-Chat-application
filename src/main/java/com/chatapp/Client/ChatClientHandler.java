package com.chatapp.Client;

import com.chatapp.models.connUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class ChatClientHandler extends SimpleChannelInboundHandler<String> {
    private ChannelHandlerContext ctx;
    private static final Logger _logger = LoggerFactory.getLogger(ChatClientHandler.class);
    private Consumer<String> messageConsumer;
    private boolean isConnected = false;

    public void setMessageConsumer(Consumer<String> messageConsumer) {
        this.messageConsumer = messageConsumer;
    }

    public ChatClientHandler() {
    }

    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        this.ctx = ctx;
        this.isConnected = true;
        _logger.info("Client connected");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) {
        _logger.debug("Raw message received: {}", msg); // Log the raw message

        // Split the message by newline
        String[] messages = msg.split("\n");

        // Process each message
        for (String singleMsg : messages) {
            if (singleMsg.trim().isEmpty() || singleMsg.trim().equals("/")) {
                _logger.warn("Invalid message received");
            } else {
                _logger.debug("Processing message: {}", singleMsg);

                // If the message starts with "Login successful", store it and wait for the next line
                if (singleMsg.startsWith("Login successful")) {
                    _logger.debug("Login successful message detected");
                    // Store the "Login successful" message and wait for the next line
                    String loginMessage = singleMsg;
                    String jsonMessage = "";

                    // Check if there is a next line (JSON part)
                    if (messages.length > 1) {
                        jsonMessage = messages[1].trim(); // The JSON is on the next line
                        _logger.debug("Received JSON: {}", jsonMessage);

                        // Combine the login message and JSON message
                        String combinedMessage = loginMessage + "\n" + jsonMessage;

                        // Pass the combined message to the message consumer
                        if (messageConsumer != null) {
                            messageConsumer.accept(combinedMessage);
                        }
                    } else {
                        _logger.warn("Incomplete login response: Missing JSON part");
                    }
                } else {
                    // Handle other messages
                    if (messageConsumer != null) {
                        messageConsumer.accept(singleMsg);
                    }
                }
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        _logger.error(cause.getMessage(), cause);
        ctx.close();
    }

    public void sendMessage(String message){
        if(ctx != null){
            ctx.writeAndFlush(message + "\r\n");
            _logger.info("Message sent: {}" , message);
        }else{
            _logger.warn("Connection not established. Unable to send message.");
        }
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }
}
