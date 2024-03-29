package com.connectify.utils;

import com.connectify.loaders.ChatLoader;
import javafx.scene.layout.BorderPane;

import java.util.HashMap;
import java.util.Map;

public class ChatPaneFactory {
    private final Map<Integer, BorderPane> paneCache = new HashMap<>();

    public BorderPane getChatPane(int id, String name, byte[] image) {

        if (paneCache.containsKey(id)) {
            return paneCache.get(id);
        }
        BorderPane newPane = ChatLoader.loadChatPane(id,name,image);
        paneCache.put(id, newPane);
        return newPane;
    }
    public void clearChats(){
        paneCache.clear();
    }

}
