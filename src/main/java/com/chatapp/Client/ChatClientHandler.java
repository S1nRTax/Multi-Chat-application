package com.chatapp.Client;

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
        String[] messages = msg.split("/n");
        for(String singleMsg : messages) {
            if (singleMsg.trim().isEmpty() || singleMsg.trim().equals("/")) {
                _logger.warn("Invalid message received");
            }else{
                _logger.debug("Server received: {}", singleMsg);
                if (messageConsumer != null) {
                    messageConsumer.accept(singleMsg);
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
