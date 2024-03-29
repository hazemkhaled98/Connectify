package com.connectify.utils;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.controlsfx.control.Notifications;

public class NotificationsManager {

    private static final double VOLUME = 0.5;

    public static void showServerNotification(String title, String body){
        Platform.runLater(() -> {
            playServerNotificationSound();
            Image icon = new Image(NotificationsManager.class.getResource("/images/notification.png").toString());
            Notifications.create()
                    .title(title)
                    .text(body)
                    .graphic(new ImageView(icon))
                    .threshold(3, Notifications.create().title("Collapsed Notification"))
                    .show();
        });
    }
    public static void showMessageNotification(String title, String body){
        Platform.runLater(() -> {
            playServerNotificationSound();
            Image icon = new Image(NotificationsManager.class.getResource("/images/message-notification.png").toString());
            Notifications.create()
                    .title(title)
                    .text(body)
                    .graphic(new ImageView(icon))
                    .threshold(3, Notifications.create().title("Collapsed Notification"))
                    .show();
        });
    }

    public static void showErrorNotification(){
        Platform.runLater(() -> {
            playErrorSound();
            Image icon = new Image(NotificationsManager.class.getResource("/images/error.png").toString());
            Notifications.create()
                    .title("Failed to connect to the Server")
                    .text("Server is down. Contact the admin and try again later")
                    .graphic(new ImageView(icon))
                    .threshold(3, Notifications.create().title("Collapsed Notification"))
                    .show();
        });
    }

    private static void playErrorSound() {
        Media sound = new Media((NotificationsManager.class.getResource("/sounds/error.mp3").toString()));
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setVolume(VOLUME);
        mediaPlayer.play();
    }

    private static void playServerNotificationSound(){
        Media sound = new Media((NotificationsManager.class.getResource("/sounds/notifications.mp3").toString()));
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setVolume(VOLUME);
        mediaPlayer.play();
    }
}
