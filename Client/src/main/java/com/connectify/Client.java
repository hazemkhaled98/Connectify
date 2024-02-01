package com.connectify;

import com.connectify.Interfaces.ConnectedUser;
import com.connectify.Interfaces.ServerAPI;
import com.connectify.utils.CurrentUser;
import com.connectify.utils.StageManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;

public class Client extends Application {

    private final static String host = "localhost";
    private final static int port = 1099;

    private static Registry registry;

    private static ConnectedUser connectedUser;

    private static Properties userCredentials;

    private static Stage primaryStage;

    @Override
    public void init() throws Exception {
        super.init();
        registry = LocateRegistry.getRegistry(host, port);
        userCredentials = new Properties();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("user.properties")) {
            userCredentials.load(is);
        } catch (IOException ex) {
            System.err.println("Could load user credentials: " + ex.getMessage());;
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        if(userCredentials.getProperty("remember").equals("true")){
            StageManager.getInstance().switchToHome();
            connectedUser = new CurrentUser(userCredentials.getProperty("phoneNumber"));
            try {
                ServerAPI server = (ServerAPI) registry.lookup("server");
                server.registerConnectedUser(connectedUser);
            } catch (NotBoundException e) {
                System.err.println("Server not found: " + e.getMessage());
            }
        }
        else{
            StageManager.getInstance().switchToLogin();
        }
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Registry getRegistry(){
        return registry;
    }

    public static ConnectedUser getConnectedUser() {
        return connectedUser;
    }

    public static void setConnectedUser(ConnectedUser connectedUser) {
        Client.connectedUser = connectedUser;
    }

    public static Properties getUserCredentials() {
        return userCredentials;
    }

    public static void updateUserCredentials(String phoneNumber, String country, String remember) {
        userCredentials.setProperty("phoneNumber", phoneNumber);
        userCredentials.setProperty("country", country);
        userCredentials.setProperty("remember", remember);
        try (FileOutputStream fos = new FileOutputStream("Client/target/classes/user.properties")) {
            userCredentials.store(fos, null);
        } catch (IOException ex) {
            System.err.println("Couldn't save credentials to file: " + ex.getMessage());
        }
    }

    public static void updateUserCredentials(String remember) {
        userCredentials.setProperty("phoneNumber", userCredentials.getProperty("phoneNumber"));
        userCredentials.setProperty("country", userCredentials.getProperty("country"));
        userCredentials.setProperty("remember", remember);
        try (FileOutputStream fos = new FileOutputStream("Client/target/classes/user.properties")) {
            userCredentials.store(fos, null);
        } catch (IOException ex) {
            System.err.println("Couldn't save credentials to file: " + ex.getMessage());
        }
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}
