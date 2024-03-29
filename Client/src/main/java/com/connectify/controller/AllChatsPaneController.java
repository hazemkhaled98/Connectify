package com.connectify.controller;

import com.connectify.dto.ChatCardsInfoDTO;
import com.connectify.loaders.ChatCardLoader;
import com.connectify.loaders.ViewLoader;
import com.connectify.utils.ChatBot;
import com.connectify.utils.CurrentUser;
import com.connectify.utils.RemoteManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.controlsfx.control.ToggleSwitch;

import java.net.URL;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class AllChatsPaneController implements Initializable {

    ViewLoader loader = ViewLoader.getInstance();

    @FXML
    private TextField chatSearchTextField;

    @FXML
    private AnchorPane allChatsAnchorPane;

    @FXML
    private ScrollPane allChatsScrollPane;

    @FXML
    private VBox allChatsVBox;
    @FXML
    private ToggleSwitch chatBotToggleSwitch;

    @FXML
    private ListView<AnchorPane> allChatsListView;
    private static ObservableList<AnchorPane> chatsPanesList = FXCollections.observableArrayList();

    private static String currentUserId;
    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentId) {
        currentUserId = currentId;
    }

    public AllChatsPaneController(){
    }

    public SortedList<AnchorPane> sortedAnchorPanes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeListView();
        loadAllUserChats();
        System.out.println("done");
        chatBotToggleSwitch.selectedProperty().bindBidirectional(ChatBot.enabledProperty());
    }

    private void initializeListView(){
        sortedAnchorPanes = new SortedList<>(chatsPanesList, (pane1, pane2) -> {
            ChatCardController controller1 = CurrentUser.getChatManagerFactory().getChatManager(ChatCardLoader.getChatsCardId(pane1)).getChatCardController();
            ChatCardController controller2 = CurrentUser.getChatManagerFactory().getChatManager(ChatCardLoader.getChatsCardId(pane2)).getChatCardController();
            return Objects.requireNonNull(controller2).getLastMessageTimestamp().compareTo(Objects.requireNonNull(controller1).getLastMessageTimestamp());
        });
        allChatsListView.setItems(sortedAnchorPanes);
        setListViewCellFactory();
        CurrentUser.setAllChatsController(this);
    }

    private void setListViewCellFactory() {
        allChatsListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<AnchorPane> call(ListView<AnchorPane> listView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(AnchorPane item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            //Platform.runLater(()->setGraphic(null));
                            setGraphic(null);
                        } else {
                            //Platform.runLater(()->setGraphic(item));
                            setGraphic(item);
                        }
                    }
                };
            }
        });
    }

    public void addChatOnChatPane(int chatId, int unread, String name, byte[] picture, String lastMessage, Timestamp timestamp){
        AnchorPane chatCard = ChatCardLoader.loadChatCardAnchorPane(chatId, unread,name,picture,lastMessage,timestamp);
        chatsPanesList.add(chatCard);
    }
    private void loadAllUserChats(){
        try {
            List<ChatCardsInfoDTO> chatCardsInfoDTOS = RemoteManager.getInstance().getUserChatsCardsInfo(CurrentUser.getInstance().getPhoneNumber());
            for(ChatCardsInfoDTO chat:chatCardsInfoDTOS)
                addChatOnChatPane(chat.getChatID(),chat.getUnreadMessagesNumber(),chat.getName(),chat.getPicture(),chat.getLastMessage(),chat.getTimestamp());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
    public void clearChatsCardList(){
        chatsPanesList.clear();
    }

    public ObservableList<AnchorPane> getChatsPanesList() {
        return chatsPanesList;
    }

    public void rearrangeChatCardController(AnchorPane chatCard){
        Platform.runLater(()->{
            chatsPanesList.remove(chatCard);
            chatsPanesList.add(chatCard);
        });
    }
    public void addLastCreatedChatCard(){
        ChatCardsInfoDTO chat;
        try {
            chat = RemoteManager.getInstance().getUserLastChatCardInfo(CurrentUser.getInstance().getPhoneNumber());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        AnchorPane chatCard = ChatCardLoader.loadChatCardAnchorPane(chat.getChatID(),chat.getUnreadMessagesNumber(),chat.getName(),chat.getPicture(),chat.getLastMessage(),chat.getTimestamp());
        chatsPanesList.add(chatCard);
    }

}
