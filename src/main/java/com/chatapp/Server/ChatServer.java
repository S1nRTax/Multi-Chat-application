package com.chatapp.Server;


import com.chatapp.Client.ChatClient;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatServer {
    private final int port;
    private static final Logger _logger = LoggerFactory.getLogger(ChatServer.class);

    public ChatServer(int port) {
        this.port = port;
    }

    public void start() throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new ChatServerHandler());
                        }
                    });

            ChannelFuture future = bootstrap.bind("localhost", port).sync();
            // starts the console command handlers as daemon thread to not block the server shutdown.
            ChatServerHandler chatServerHandler = new ChatServerHandler();
            Thread consoleThread = new Thread(new ConsoleCommandHandler(chatServerHandler));
            consoleThread.setDaemon(true);

            consoleThread.start();


            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void shutdown() {
        ChatServerHandler.getChannels().close().awaitUninterruptibly();
        _logger.info("Server shutdown complete.");
    }


    public static void main(String[] args) throws InterruptedException {
        new ChatServer(8080).start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            _logger.info("Shutting down server...");
            ChatServer.shutdown();
        }));
    }
}
