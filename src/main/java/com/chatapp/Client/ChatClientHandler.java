package com.chatapp.Client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatClientHandler extends SimpleChannelInboundHandler<String> {
    private final String username;
    private final char[] password;
    private ChannelHandlerContext ctx;
    private static final Logger _logger = LoggerFactory.getLogger(ChatClientHandler.class);

    public ChatClientHandler(String username, char[] password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        _logger.info("Client connected");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) {
        String[] messages = msg.split("/n");
        for(String singleMsg : messages){
            if(singleMsg.trim().isEmpty() || singleMsg.trim().equals("/")){
                _logger.warn("Invalid message received");}
            else{
                _logger.info("Server received: {}", singleMsg);
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

}
