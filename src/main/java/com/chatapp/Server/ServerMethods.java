package com.chatapp.Server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import org.slf4j.*;
import com.chatapp.database.DAO;

import java.sql.SQLException;
import java.util.Map;

public class ServerMethods {
    private final Map<String, ChannelHandlerContext> authenticatedUsers;
    private final ChannelGroup channels;

    private static final Logger _logger = LoggerFactory.getLogger(ServerMethods.class);

    public ServerMethods(Map<String, ChannelHandlerContext> authenticatedUsers, ChannelGroup channels, DAO dao) {
        this.authenticatedUsers = authenticatedUsers;
        this.channels = channels;
    }

    public void handleLogin(ChannelHandlerContext ctx,String message) {
        message = message.trim();
        String[] parts = message.split(" ",3);
        if(parts.length == 3){
            String username = parts[1];
            String password = parts[2];
            try{
                DAO.InsertResult error = DAO.loginUser(username, password);
                _logger.debug(error.getMessage());
                if(error.equals(DAO.InsertResult.SUCCESS)){
                    authenticatedUsers.put(username, ctx);
                    ctx.writeAndFlush("Login successful\r\n");
                    _logger.info("User {} logged in",username);
                    // TODO: load friend list.
                }else if(error.equals(DAO.InsertResult.User_Not_Found)){
                    ctx.writeAndFlush("Authentication Failed : " + error.getMessage());
                }else if(error.equals(DAO.InsertResult.Incorrect_Password)){
                    ctx.writeAndFlush("Authentication Failed : " + error.getMessage());
                }

            }catch(Exception e){
                _logger.error(e.getMessage(), e);
                ctx.writeAndFlush("Internal error");
            }
        }else{
            _logger.error("Invalid login format. Use: /login username password\n");
            ctx.writeAndFlush("Invalid login format. Use: /login username password\n");
        }
    }


    public void handleRegister(ChannelHandlerContext ctx, String message) {
        message = message.trim();
        String[] parts = message.split(" ",4);
        if(parts.length == 4){
            String username = parts[1];
            String email = parts[2];
            String password = parts[3];

            try{
                DAO.InsertResult error = DAO.registerUser(username, email, password);
                if(error == DAO.InsertResult.SUCCESS){
                    ctx.writeAndFlush("Registration successful");
                    _logger.info("User {} registered", username);
                }else{
                    ctx.writeAndFlush("Problem registering user " + username + ". Try again later.\n");
                    _logger.error("Problem registering user {} - {}. Try again later.", username, error.getMessage());
                }
            }catch (Exception e){
                _logger.error("Internal error");
                _logger.error(e.getMessage(), e);
            }
        }
    }

}
