package com.connectify.services;

import com.connectify.Interfaces.ConnectedUser;
import com.connectify.app.Server;
import com.connectify.controller.ServerController;
import com.connectify.dto.ChatCardsInfoDTO;
import com.connectify.dto.IncomingFriendInvitationResponse;
import com.connectify.model.dao.InvitationsDAO;
import com.connectify.model.dao.impl.InvitationsDAOImpl;
import com.connectify.model.dao.impl.UserDAOImpl;
import com.connectify.model.entities.Invitations;

import java.rmi.RemoteException;
import java.util.List;

public class InvitationService {
    public boolean sendInvitation(String senderPhoneNumber, String receiverPhoneNumber) {
        InvitationsDAO invitationsDAO = new InvitationsDAOImpl();
        Invitations friendInvitation = new Invitations();
        friendInvitation.setSender(senderPhoneNumber);
        friendInvitation.setReceiver(receiverPhoneNumber);
        boolean isInvitationSent = invitationsDAO.insert(friendInvitation);
        if (isInvitationSent) {
            try {
                IncomingFriendInvitationResponse receivedInvitation = invitationsDAO
                        .getIncomingFriendRequest(senderPhoneNumber, receiverPhoneNumber);
                ConnectedUser receiver = Server.getConnectedUsers().get(receiverPhoneNumber);
                if (receiver != null) {
                    receiver.receiveNotification("New Friend Request", "You have received a new friend request");
                }
            } catch (RemoteException e) {
                System.err.println("Error sending friend invitation. case:" + e.getMessage());
            }
        }
        return isInvitationSent;
    }

    public int isInvitationSent(String senderPhoneNumber, String receiverPhoneNumber) {
        InvitationsDAO invitationsDAO = new InvitationsDAOImpl();
        return invitationsDAO.isInvitationSent(senderPhoneNumber, receiverPhoneNumber);
    }

    public List<IncomingFriendInvitationResponse> getIncomingFriendRequests(String phoneNumber) {
        InvitationsDAO invitationsDAO = new InvitationsDAOImpl();
        List<IncomingFriendInvitationResponse> list = invitationsDAO.getIncomingFriendRequests(phoneNumber);
        return list;
    }

    public boolean acceptFriendRequest(int invitationId) {
        InvitationsDAO invitationsDAO = new InvitationsDAOImpl();
        Invitations friendInvitation = invitationsDAO.get(invitationId);
        boolean isInvitationAccepted = invitationsDAO.acceptFriendRequest(invitationId);
        if (isInvitationAccepted) {
            try {
                ConnectedUser receiver = Server.getConnectedUsers().get(friendInvitation.getSender());
                if (receiver != null) {
                    String notificationTitle = "New Friendship";
                    String receiverName = new UserDAOImpl().get(friendInvitation.getReceiver()).getName();
                    String notificationMessage = receiverName + " has accepted your friend request.";
                    receiver.receiveNotification(notificationTitle, notificationMessage);
                    ServerController controller = new ServerController();
                    ChatCardsInfoDTO chatCard = controller.getUserLastChatCardInfo(receiver.getPhoneNumber());
                    receiver.makeNewChatCard(chatCard);
                }
            } catch (RemoteException e) {
                System.err.println("Error sending friend invitation. Case: " + e.getMessage());
            }
        }

        return isInvitationAccepted;
    }


    public boolean cancelFriendRequest(int invitationId) {
        InvitationsDAO invitationsDAO = new InvitationsDAOImpl();
        return invitationsDAO.delete(invitationId);
    }
}
