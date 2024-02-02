package com.connectify.services;

import com.connectify.Interfaces.ConnectedUser;
import com.connectify.Server;
import com.connectify.dto.LoginRequest;
import com.connectify.dto.LoginResponse;
import com.connectify.dto.SignUpRequest;
import com.connectify.mapper.UserMapper;
import com.connectify.model.dao.UserDAO;
import com.connectify.model.dao.impl.UserDAOImpl;
import com.connectify.model.entities.User;
import com.connectify.model.enums.Mode;
import com.connectify.model.enums.Status;
import com.connectify.util.PasswordManager;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class UserService {

    public boolean insertUser(SignUpRequest request){
        User user = UserMapper.INSTANCE.signUpRequestToUser(request);
        user.setMode(Mode.ONLINE);
        user.setStatus(Status.AVAILABLE);
        UserDAO dao = new UserDAOImpl();
        return dao.insert(user);
    }

    public LoginResponse loginUser(LoginRequest request){
        UserDAO userDAO = new UserDAOImpl();
        User user = userDAO.get(request.getPhoneNumber());
        if(user == null){
            return new LoginResponse(false, "Phone number is not correct or registered");
        }
        String hashedPassword = user.getPassword();
        boolean isCorrect = PasswordManager.isEqual(hashedPassword, request.getPassword(), user.getSalt());
        if(isCorrect){
            userDAO.updateMode(user.getPhoneNumber(), Mode.ONLINE);
            return new LoginResponse(true, "Login successful");
        }
        return new LoginResponse(false, "Password is not correct");
    }

    public boolean logoutUser(String phoneNumber){
        UserDAO userDAO = new UserDAOImpl();
        return userDAO.updateMode(phoneNumber, Mode.OFFLINE);
    }

    public void registerConnectedUser(ConnectedUser user) {
        try {
            Server.getConnectedUsers().put(user.getPhoneNumber(), user);
            System.out.println("Registered user: " + user.getPhoneNumber());
        } catch (RemoteException e) {
            System.err.println("Remote Exception: " + e.getMessage());
        }
    }

    public void unregisterConnectedUser(ConnectedUser user) {
        try {
            Server.getConnectedUsers().remove(user.getPhoneNumber());
            System.out.println("Unregistered user: " + user.getPhoneNumber());
        } catch (RemoteException e) {
            System.err.println("Remote Exception: " + e.getMessage());
        }
    }
}