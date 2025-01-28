package com.chatapp.Server;

import com.chatapp.database.DAO;
import com.chatapp.database.DatabaseInitializer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

    private static final Logger _logger = LoggerFactory.getLogger(ChatServerHandler.class);
    private static final Map<String, ChannelHandlerContext> authenticatedUsers = new HashMap<>();
    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    DAO dao = new DAO();
    DatabaseInitializer dbInit = new DatabaseInitializer();

    ServerMethods serverMethods =  new ServerMethods(
            authenticatedUsers,
            channels,
            dao
    );

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        channels.add(ctx.channel());
        _logger.info("Client connected: {}", ctx.channel().remoteAddress());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        channels.remove(ctx.channel());
        _logger.info("Client disconnected: {}", ctx.channel().remoteAddress());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String message) throws Exception {
            _logger.info("Server received: {}", message);
            if(message.startsWith("/login ")) {
                serverMethods.handleLogin(ctx, message);
            }else if(message.startsWith("/register ")) {
                serverMethods.handleRegister(ctx, message);
            }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        _logger.error(cause.getMessage(), cause);
        ctx.close();
    }

    public static ChannelGroup getChannels(){
        return channels;
    }

}
