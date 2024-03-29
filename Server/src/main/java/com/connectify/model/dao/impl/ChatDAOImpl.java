package com.connectify.model.dao.impl;

import com.connectify.model.dao.ChatDAO;
import com.connectify.model.entities.Chat;
import com.connectify.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChatDAOImpl implements ChatDAO {
    private final DBConnection dbConnection;

    public ChatDAOImpl() {
        dbConnection = DBConnection.getInstance();
    }
    @Override
    public boolean insert(Chat chat) {
        String query = "INSERT INTO Chat (is_Private_Chat, number_Of_Members) VALUES (?, ?, ?)";
        try(Connection connection = dbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            preparedStatement.setInt(1, chat.getIsPrivateChat());
            preparedStatement.setInt(2, chat.getNumberOfMembers());
            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Chat get(Integer key) {
        String query = "SELECT * FROM Chats WHERE chat_id = ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            preparedStatement.setInt(1, key);
            Chat chat = null;
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    chat = new Chat();
                    chat.setChatId(resultSet.getInt("chat_id"));
                    chat.setIsPrivateChat(resultSet.getInt("is_Private_Chat"));
                    chat.setNumberOfMembers(resultSet.getInt("number_Of_Members"));
                }
            }
            return chat;
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean update(Chat chat) {
        String query = "UPDATE chats SET number_Of_Members WHERE chat_id = ?";
        try(Connection connection = dbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            preparedStatement.setInt(1, chat.getNumberOfMembers());
            int rowUpdated = preparedStatement.executeUpdate();
            return rowUpdated > 0;
        } catch (SQLException e){
            System.err.println("SQLException: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Integer key) {
        String query = "DELETE FROM chats WHERE chat_id = ?";
        try(Connection connection = dbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            preparedStatement.setInt(1, key);
            int rowDeleted = preparedStatement.executeUpdate();
            return rowDeleted > 0;
        } catch (SQLException e){
            System.err.println("SQLException: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isPrivateChat(int chatID) {
        String query = "SELECT is_Private_Chat FROM connectify_db.chat WHERE chat_id = ?;";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            preparedStatement.setInt(1, chatID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBoolean("is_Private_Chat");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
            return false;
        }
        return false;
    }
}
