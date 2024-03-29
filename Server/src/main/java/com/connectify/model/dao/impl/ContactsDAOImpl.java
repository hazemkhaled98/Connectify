package com.connectify.model.dao.impl;

import com.connectify.model.dao.ContactsDAO;
import com.connectify.model.entities.Contacts;
import com.connectify.model.entities.User;
import com.connectify.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactsDAOImpl implements ContactsDAO {

    private final DBConnection dbConnection;

    public ContactsDAOImpl() {
        dbConnection = DBConnection.getInstance();
    }

    @Override
    public boolean insert(Contacts contacts) {
        String query = "INSERT INTO contacts (user, contact) VALUES (?, ?)";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, contacts.getUser());
            preparedStatement.setString(2, contacts.getContact());

            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Contacts get(String key) {
        String query = "SELECT * FROM contacts WHERE user = ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, key);
            Contacts contacts = null;
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    contacts = new Contacts();
                    contacts.setUser(resultSet.getString("user"));
                    contacts.setContact(resultSet.getString("contact"));
                }
            }
            return contacts;
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean update(Contacts contacts) {
        String query = "UPDATE contacts SET contact = ? WHERE user = ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, contacts.getContact());
            preparedStatement.setString(2, contacts.getUser());

            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(String key) {
        String query = "DELETE FROM contacts WHERE user = ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, key);
            int rowsDeleted = preparedStatement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<User> getContactsList(String PhoneNumber) {

        String query = "SELECT friend,u.name,u.picture FROM users u JOIN ( " +
                " (SELECT user AS friend FROM contacts WHERE contact = ? ) " +
                " UNION " +
                " (SELECT contact AS friend FROM contacts WHERE user = ? ) " +
                " ) AS friends ON u.phone_number =friends.friend;";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, PhoneNumber);
            preparedStatement.setString(2, PhoneNumber);
            List<User> contactsList = new ArrayList<>();
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    User user =new User();
                    user.setPhoneNumber(resultSet.getString(1));
                    user.setName(resultSet.getString(2));
                    user.setPicture(resultSet.getBytes(3));
                    contactsList.add(user);
                }
            }
            return contactsList;
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean areAlreadyFriends(String userPhone, String friendPhone) {
        String query = "SELECT COUNT(*) FROM contacts WHERE (user = ? AND contact = ?) OR (user = ? AND contact = ?)";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, userPhone);
            preparedStatement.setString(2, friendPhone);
            preparedStatement.setString(3, friendPhone);
            preparedStatement.setString(4, userPhone);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }

        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
            return false;
        }

        return false;
    }

}
