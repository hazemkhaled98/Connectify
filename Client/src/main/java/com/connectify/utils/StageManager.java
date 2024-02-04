package com.connectify.utils;

import com.connectify.Client;
import com.connectify.loaders.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.HashMap;
import java.util.Map;

public class StageManager {

    private static StageManager instance;

    private final Stage stage;

    private final Map<String, Scene> sceneMap;
    private AnchorPane chatsPane;
    private AnchorPane logoPane;
    private AnchorPane incomingFriendRequestPane;


    private StageManager(){
        stage = Client.getPrimaryStage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setMinWidth(750);
        stage.setMinHeight(500);
        sceneMap = new HashMap<>();
    }


    public static StageManager getInstance(){
        if(instance == null){
            instance = new StageManager();
        }
        return instance;
    }

    public void switchToLogin(){
        if(sceneMap.get("login") == null)
            sceneMap.put("login", createLoginScene());
        Scene scene = sceneMap.get("login");
        stage.setScene(scene);
    }

    public void switchToSignUp(){
        if(sceneMap.get("signup") == null)
            sceneMap.put("signup", createSignUpScene());
        Scene scene = sceneMap.get("signup");
        stage.setScene(scene);
    }


    public void switchToHome(){
        if(sceneMap.get("home") == null)
            sceneMap.put("home", createHomeScene());
        Scene scene = sceneMap.get("home");
        stage.setScene(scene);
    }

    public void switchFromProfileEditorToHome(){
        Scene scene = sceneMap.get("home");
        BorderPane mainPane = (BorderPane) scene.getRoot();
        BorderPane centerPane = (BorderPane) mainPane.getCenter();
        centerPane.setCenter(logoPane);
        centerPane.setLeft(chatsPane);
    }

    public void switchFromAddFriendToHome(){
        Scene scene = sceneMap.get("home");
        BorderPane mainPane = (BorderPane) scene.getRoot();
        BorderPane centerPane = (BorderPane) mainPane.getCenter();
        centerPane.setCenter(logoPane);
        centerPane.setLeft(chatsPane);
    }

    public void switchToProfile(){
        Scene scene = sceneMap.get("home");
        BorderPane mainPane = (BorderPane) scene.getRoot();
        BorderPane centerPane = (BorderPane) mainPane.getCenter();
        centerPane.setCenter(ProfileLoader.loadProfileAnchorPane());
        centerPane.setLeft(chatsPane);
    }

    public void switchToProfileEditor(){
        Scene scene = sceneMap.get("home");
        BorderPane mainPane = (BorderPane) scene.getRoot();
        BorderPane centerPane = (BorderPane) mainPane.getCenter();
        centerPane.setCenter(ProfileEditorLoader.loadProfileEditorAnchorPane());
        centerPane.setLeft(chatsPane);
    }

    public void switchToAddFriend(){
        Scene scene = sceneMap.get("home");
        BorderPane mainPane = (BorderPane) scene.getRoot();
        BorderPane centerPane = (BorderPane) mainPane.getCenter();
        centerPane.setLeft(AddFriendLoader.loadAddFriendAnchorPane());
        centerPane.setCenter(logoPane);
    }

    public void switchToIncomingFriendRequest(){
        Scene scene = sceneMap.get("home");
        BorderPane mainPane = (BorderPane) scene.getRoot();
        BorderPane centerPane = (BorderPane) mainPane.getCenter();
        if (incomingFriendRequestPane == null)
            incomingFriendRequestPane = IncomingFriendRequestLoader.loadIncomingFriendRequestAnchorPane();
        centerPane.setLeft(incomingFriendRequestPane);
    }

    private Scene createLoginScene(){
        BorderPane mainPane = new BorderPane();
        GridPane centerPane = new GridPane();
        AnchorPane logoPane = LogoLoader.loadLogoAnchorPane();
        AnchorPane loginPane = LoginLoader.loadLoginAnchorPane();
        centerPane.add(logoPane,0,0);
        centerPane.add(loginPane,1,0);
        HBox titleBar = TitleBarLoader.loadTitleBarHBox();
        mainPane.setTop(titleBar);
        mainPane.setCenter(centerPane);
        return new Scene(mainPane);
    }

    private Scene createSignUpScene(){
        BorderPane mainPane = new BorderPane();
        GridPane centerPane = new GridPane();
        AnchorPane logoPane = LogoLoader.loadLogoAnchorPane();
        AnchorPane signUpPane = SignUpLoader.loadSignUpAnchorPane();
        centerPane.add(logoPane,0,0);
        centerPane.add(signUpPane,1,0);
        HBox titleBar = TitleBarLoader.loadTitleBarHBox();
        mainPane.setTop(titleBar);
        mainPane.setCenter(centerPane);
        return new Scene(mainPane);
    }

    private Scene createHomeScene(){
        BorderPane mainPane = new BorderPane();
        HBox titleBar = TitleBarLoader.loadTitleBarHBox();
        BorderPane centerPane = new BorderPane();
        AnchorPane optionsPane = HomeScreenOptionsLoader.loadHomeScreenOptionsAnchorPane();
        chatsPane = AllChatsPaneLoader.loadAllChatsAnchorPane();
        logoPane = LogoLoader.loadLogoAnchorPane();
        centerPane.setLeft(chatsPane);
        centerPane.setCenter(logoPane);
        mainPane.setLeft(optionsPane);
        mainPane.setTop(titleBar);
        mainPane.setCenter(centerPane);
        return new Scene(mainPane);
    }

}
