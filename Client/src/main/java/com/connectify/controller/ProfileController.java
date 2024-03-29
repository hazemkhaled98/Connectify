package com.connectify.controller;

import com.connectify.dto.UserProfileResponse;
import com.connectify.model.enums.Gender;
import com.connectify.model.enums.Mode;
import com.connectify.model.enums.Status;
import com.connectify.utils.CurrentUser;
import com.connectify.utils.RemoteManager;
import com.connectify.utils.StageManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {

    @FXML private DatePicker birthDatePicker;
    @FXML private TextField emailTxtF;
    @FXML private ComboBox<Gender> genderComboBox;
    @FXML private ComboBox<Status> statusComboBox;
    @FXML private ComboBox<Mode> modeComboBox;
    @FXML private TextField nameTxtF;
    @FXML private TextField phoneNumTxtF;
    @FXML private AnchorPane profilePane;
    @FXML private TextArea bioTextArea;
    @FXML private Button editeBtn;
    @FXML private Label profileLbl;
    @FXML private Circle userImg;

    private String txtFieldsOriginalStyle, comboBoxOriginalStyle, datePickerOriginalStyle;

    private UserProfileResponse currentUserDetails;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            currentUserDetails = RemoteManager.getInstance().getUserProfile(CurrentUser.getInstance().getPhoneNumber());
        } catch (RemoteException e) {
            System.err.println("Remote Exception: " + e.getMessage());
        }
        populateUserDetails();
    }

    private void populateUserDetails() {
        setImage();
        bioTextArea.setText(currentUserDetails.getBio() == null ? "bio" : currentUserDetails.getBio());
        phoneNumTxtF.setText(currentUserDetails.getPhoneNumber());
        nameTxtF.setText(currentUserDetails.getName());
        emailTxtF.setText(currentUserDetails.getEmail());
        birthDatePicker.setValue(currentUserDetails.getBirthDate());
        genderComboBox.setValue(currentUserDetails.getGender());
        statusComboBox.setValue(currentUserDetails.getStatus());
        modeComboBox.setValue(currentUserDetails.getMode());

        birthDatePicker.setDisable(true);
        genderComboBox.setDisable(true);
        statusComboBox.setDisable(true);
        modeComboBox.setDisable(true);
    }

    private void setImage() {
        Image image = null;
        if (currentUserDetails.getPicture() == null) {
            image = new Image(String.valueOf(ProfileController.class.getResource("/images/profile.png")));
        } else {
            image = new Image(new ByteArrayInputStream(currentUserDetails.getPicture()));
        }
        userImg.setFill(new ImagePattern(image));
    }

    @FXML
    private void editeBtnHandler(ActionEvent event){
        StageManager.getInstance().switchToProfileEditor();
    }
}
