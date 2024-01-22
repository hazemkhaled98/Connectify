package com.connectify.loaders;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
public class ViewLoader {


    BorderPane mainBorderPane;
    AnchorPane logoAnchorPane;
    AnchorPane signUpAnchorPane;
    AnchorPane loginAnchorPane;

    AnchorPane homeScreenOptionsPane;

    AnchorPane allChatsAnchorPane;

    HBox titleBarHBox;
    AnchorPane chatCardHBox;

    private ViewLoader(){
        loadMainPane();
        //loadTitleBar();
        loadLogoPane();
        loadLoginPane();
        loadSignUpPane();
        loadMainBorderPane();
        loadChatCardHBox();
        loadTitleBar();
        loadLogoAnchorPane();
        loadSignUpAnchorPane();
        loadHomeScreenOptionsPane();
        loadAllChatsAnchorPane();
    }
    private void loadMainPane() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/MainPane.fxml"));
        try {
            mainBorderPane =fxmlLoader.load();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadSignUpPane(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/SignUpPane.fxml"));
        try {
            signUpAnchorPane = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    BorderPane chatWindow;

    private void loadAllChatsAnchorPane(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/AllChatsPane.fxml"));
        try {
            allChatsAnchorPane =fxmlLoader.load();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void loadMainBorderPane() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/MainPane.fxml"));
        try {
            mainBorderPane =fxmlLoader.load();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadTitleBar(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/titleBarPane.fxml"));
        try {
            titleBarHBox =fxmlLoader.load();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void loadSignUpAnchorPane(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/SignUpPane.fxml"));
        try {
            signUpAnchorPane = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadLoginPane(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/LoginPane.fxml"));
        try {
            loginAnchorPane = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadLogoPane(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/logoPane.fxml"));
        try {
            logoAnchorPane = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void loadLogoAnchorPane(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/logoPane.fxml"));
        try {
            logoAnchorPane = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadChatPane() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/Chat.fxml"));
        try {
            chatWindow = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void loadHomeScreenOptionsPane(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/HomeScreenOptionsPane.fxml"));
        try {
            homeScreenOptionsPane = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadUserMessage(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/UserMessage.fxml"));
        try {
            logoAnchorPane = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void loadChatCardHBox(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/ChatCardPane.fxml"));
        try {
            chatCardHBox = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadContactMessage(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/ContactMessage.fxml"));
        try {
            logoAnchorPane = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final ViewLoader viewLoader = new ViewLoader();
    public static ViewLoader getInstance() {
        return viewLoader;
    }



    public AnchorPane getSignUpAnchorPane() {
        return signUpAnchorPane;
    }


    public BorderPane getMainBorderPane() {
        return mainBorderPane;
    }

    public AnchorPane getLogoAnchorPane() {
        return logoAnchorPane;
    }

    public AnchorPane getLoginAnchorPane() {
        return loginAnchorPane;
    }

    public HBox getTitleBarHBox() {
        return titleBarHBox;
    }

    public AnchorPane getHomeScreenOptionsPane() {
        return homeScreenOptionsPane;
    }

    public AnchorPane getChatCardHBox() {
        return chatCardHBox;
    }

    public AnchorPane getAllChatsAnchorPane() {
        return allChatsAnchorPane;
    }

    /*public VBox getAllChatsAnchor() {
        return (VBox) allChatsAnchorPane.getContent();
    }*/
}
