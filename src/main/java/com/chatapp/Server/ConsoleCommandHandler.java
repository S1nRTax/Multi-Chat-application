package com.chatapp.Server;

import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleCommandHandler implements Runnable{
    private static final Logger _logger = LoggerFactory.getLogger(ConsoleCommandHandler.class);
    private final ChatServerHandler chatServerHandler;

    public ConsoleCommandHandler(ChatServerHandler chatServerHandler) {
        this.chatServerHandler = chatServerHandler;
    }

    @Override
    public void run(){
        Scanner scanner = new Scanner(System.in);

        while(true){
            _logger.info("Enter command: ");
            String command = scanner.nextLine();

            if(command.equalsIgnoreCase("/shutdown")){
                _logger.info("Shutting down chat server...");
                ChatServer.shutdown();
                break;
            }else if(command.equalsIgnoreCase("/help")){
                _logger.info("Available commands: ");
                _logger.info("/shutdown - Shuts down the server");
            }else{
                _logger.info("Unknown command: [{}]. Type /help for a list of commands.",command);
            }
        }
    }

}
