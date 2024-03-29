package com.connectify.controller;

import com.connectify.dto.ContactsDTO;
import com.connectify.loaders.ContactCardLoader;
import com.connectify.mapper.ContactMapper;
import com.connectify.model.entities.User;
import com.connectify.utils.CurrentUser;
import com.connectify.utils.RemoteManager;
import com.connectify.utils.StageManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;

public class AllContactsPaneController implements Initializable {

    @FXML
    private TextField ContactSearchTextField;

    @FXML
    private AnchorPane allContactsAnchorPane;

    @FXML
    private ScrollPane allContactsScrollPane;

    @FXML
    private VBox allContactsVBox;

    @FXML
    private Button createGroupButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<ContactsDTO> ContactDTOList = null;
        try {
            ContactDTOList = RemoteManager.getInstance().getContacts(CurrentUser.getInstance().getPhoneNumber());
        } catch (RemoteException e) {
            System.err.println("Remote Exception: " + e.getMessage());
        }
        ContactMapper mapper=ContactMapper.INSTANCE;
        List<User> contactsList = mapper.contactDTOListToUserList(ContactDTOList);
        for (User user : contactsList)
        {
            allContactsVBox.getChildren().add(ContactCardLoader.loadContactCardAnchorPane(user));
        }
    }

    public void addContactOnContactsPane(){

    }

    @FXML
    void createGroupHandler(ActionEvent event) {
        StageManager.getInstance().switchToChooseContactsGroupPane();
    }

}
