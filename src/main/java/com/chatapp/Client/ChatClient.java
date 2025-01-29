package com.chatapp.Client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatClient {
    private final String host;
    private final int port;
    private ChatClientHandler handler;
    private static final Logger _logger = LoggerFactory.getLogger(ChatClient.class);

    public boolean isConnected() {
        return handler != null && handler.isConnected();
    }

    public ChatClientHandler getHandler() {
        return handler;
    }

    public void setHandler(ChatClientHandler handler) {
        this.handler = handler;
    }

    public ChatClient(String host, int port) {
        this.host = host;
        this.port = port;

        handler = new ChatClientHandler();
    }


    public void start() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(handler);
                        }
                    });

            _logger.info("Connecting to {}:{}", host, port);
            ChannelFuture future = bootstrap.connect(host, port).sync();
            _logger.info("Connected");

            future.channel().closeFuture().sync();
        } finally {
            _logger.info("Shutting down client...");
            group.shutdownGracefully();
        }
    }

    public void sendMessage(String message) {
        if(handler != null) {
            handler.sendMessage(message);
        }else{
            _logger.error("Handler is not initialized. Unable to send messages.");
        }
    }
}
