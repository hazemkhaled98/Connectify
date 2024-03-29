package com.connectify.controller;

import com.connectify.utils.ImageConverter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class AddFriendCardController implements Initializable {

    @FXML
    private Label contactNameTextField;

    @FXML
    private Label contactPhoneNumberLabel;

    @FXML
    private Circle contactPicture;

    @FXML
    private HBox messageHBox;

    String name;
    byte[] pictureBytes;
    String phone;

    public AddFriendCardController() {

    }

    public AddFriendCardController(String name,  String phone, byte[] pictureBytes) {
        this.name = name;
        this.phone = phone;
        this.pictureBytes = pictureBytes;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCardName(name);
        setCardPhone(phone);
        setCardPhoto(pictureBytes);
    }

    private void setCardName(String name) {
        contactNameTextField.setText(name);
    }

    private void setCardPhone(String phone) {
        contactPhoneNumberLabel.setText(phone);
    }

    private void setCardPhoto(byte[] pictureBytes) {
        this.pictureBytes = pictureBytes;
        contactPicture.setFill(ImageConverter.convertBytesToImagePattern(pictureBytes));
    }

}
