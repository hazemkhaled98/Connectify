package com.connectify.utils;

import com.connectify.Interfaces.ConnectedUser;
import com.connectify.dto.MessageDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class CurrentUser extends UnicastRemoteObject implements ConnectedUser, Serializable {

    private final String phoneNumber;

    private static final Map<Integer, ObservableList<String>> chatListMessagesMap = new HashMap<>();

    public CurrentUser(String phoneNumber) throws RemoteException {
        super();
        this.phoneNumber = phoneNumber;
    }
    @Override
    public void receiveAnnouncement(String announcement) throws RemoteException {

    }

    public String getPhoneNumber() throws RemoteException {
        return phoneNumber;
    }

    @Override
    public void receiveMessage(MessageDTO messageDTO) throws RemoteException {
        System.out.println("Received Message");
        int chatID = messageDTO.getChatId();
        chatListMessagesMap.putIfAbsent(chatID, FXCollections.observableArrayList());
        chatListMessagesMap.get(chatID).add(messageDTO.getContent());
    }

    public static ObservableList<String> getMessageList(int chatID) {
        chatListMessagesMap.putIfAbsent(chatID, FXCollections.observableArrayList());
        return chatListMessagesMap.get(chatID);
    }
}
