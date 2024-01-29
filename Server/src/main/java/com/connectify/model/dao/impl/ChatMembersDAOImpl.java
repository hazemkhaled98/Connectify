package com.connectify.model.dao.impl;

import com.connectify.model.dao.ChatMembersDAO;
import com.connectify.model.entities.ChatMember;
import com.connectify.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChatMembersDAOImpl implements ChatMembersDAO {
    private final DBConnection dbConnection;

    public ChatMembersDAOImpl() {
        dbConnection = DBConnection.getInstance();;
    }



    public ChatMember findChatMember(int chatId, String member) throws SQLException {

        return null;
    }

    @Override
    public List<ChatMember> getAllUserChats(String userId) throws SQLException {
        List<ChatMember> chatMembers = new ArrayList<>();
        String query = "SELECT chat_id, member FROM chat_members WHERE member = ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)){;
             preparedStatement.setString(1, userId);
             try(ResultSet rs = preparedStatement.executeQuery()){
                 while (rs.next()) {
                     ChatMember chatMember = new ChatMember(
                             rs.getInt("chat_Id"),
                             rs.getString("member")
//                             rs.getBoolean("is_Open"),
//                             rs.getTimestamp("last_Opened_Time"),
//                             rs.getInt("unread_Messages_Number")
                     );
                     chatMembers.add(chatMember);
                 }
             }
        }
        return chatMembers;
    }

    @Override
    public List<ChatMember> getAllChatMembers(int chatID) throws SQLException {
        List<ChatMember> chatMembers = new ArrayList<>();
        String query = "SELECT * FROM chat_members WHERE chat_id = ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)){;
            preparedStatement.setInt(1, chatID);
            try(ResultSet rs = preparedStatement.executeQuery()){
                while (rs.next()) {
                    ChatMember chatMember = new ChatMember(
                            rs.getInt("chat_Id"),
                            rs.getString("member"),
                            rs.getBoolean("is_Open"),
                            rs.getTimestamp("last_Opened_Time"),
                            rs.getInt("unread_Messages_Number")
                    );
                    chatMembers.add(chatMember);
                }
            }
        }
        return chatMembers;
    }

    @Override
    public boolean insert(ChatMember chatMember) {
        String query = "INSERT INTO chat_members (chat_Id, member) VALUES (?, ?)";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, chatMember.getChatId());
            preparedStatement.setString(2, chatMember.getMember());
            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
            return false;
        }
    }

    @Override
    public ChatMember get(ChatMember chatMember) {
        String query = "SELECT * FROM chat_members WHERE chat_Id = ? AND member = ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, chatMember.getChatId());
            preparedStatement.setString(2, chatMember.getMember());

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return new ChatMember(
                            rs.getInt("chat_Id"),
                            rs.getString("member"),
                            rs.getBoolean("is_Open"),
                            rs.getTimestamp("last_Opened_Time"),
                            rs.getInt("unread_Messages_Number")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public boolean update(ChatMember chatMember) {
        String query = "UPDATE chat_members SET is_Open = ?, last_Opened_Time = ?, unread_Messages_Number = ? WHERE chat_Id = ? AND member = ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setBoolean(1, chatMember.getIsOpen());
            preparedStatement.setTimestamp(2, chatMember.getLastOpenedTime());
            preparedStatement.setInt(3, chatMember.getUnreadMessagesNumber());
            preparedStatement.setInt(4, chatMember.getChatId());
            preparedStatement.setString(5, chatMember.getMember());
            int updatedRows = preparedStatement.executeUpdate();
            return updatedRows >0;
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(ChatMember chatMember) {
        String query = "DELETE FROM chat_members WHERE chat_Id = ? AND member = ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, chatMember.getChatId());
            preparedStatement.setString(2, chatMember.getMember());
            return preparedStatement.executeUpdate()>0;
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
            return false;
        }
    }
}
