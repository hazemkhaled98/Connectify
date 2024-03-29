package com.connectify.controller;


import com.connectify.Interfaces.ConnectedUser;
import com.connectify.Interfaces.ServerAPI;
import com.connectify.app.Server;
import com.connectify.dto.*;
import com.connectify.model.entities.User;
import com.connectify.model.enums.Mode;
import com.connectify.model.enums.Status;
import com.connectify.services.*;
import com.connectify.utils.TokenGenerator;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ServerController extends UnicastRemoteObject implements ServerAPI {

    private final UserService userService;
    private final MessageService messageService;

    private final ChatService chatService;

    private final AttachmentService attachmentService;
    private final ContactService contactService;
    private final InvitationService invitationService;
    private final ContactsService contactsService;
    private GroupService groupService;

    public ServerController() throws RemoteException {
        userService = new UserService();
        messageService = new MessageService();
        chatService = new ChatService();
        attachmentService = new AttachmentService();
        contactService =new ContactService();
        invitationService = new InvitationService();
        contactsService = new ContactsService();
        groupService = new GroupService();
    }

    @Override
    public SignUpResponse signUp(SignUpRequest signUpRequest) throws RemoteException {
        SignUpResponse response = new SignUpResponse();
        boolean isSuccessful = userService.insertUser(signUpRequest);
        if(isSuccessful){
            String token = null;
            boolean isTokenUnique = false;
            do {
                token = TokenGenerator.generateToken();
                isTokenUnique = userService.updateToken(signUpRequest.getPhoneNumber(),token);
            } while (!isTokenUnique);
            response.setSuccessful(true);
            response.setMessage("Sign up successful");
            response.setToken(token);
            return response;
        }
        else {
            response.setSuccessful(false);
            response.setMessage("Phone number is already registered");
            return response;
        }
    }

    @Override
    public String getPhoneNumberByToken(String token) throws RemoteException {
        return userService.getPhoneNumberByToken(token);
    }

    public LoginResponse login(LoginRequest loginRequest) throws RemoteException {
        LoginResponse response = userService.loginUser(loginRequest);
        if(response.getStatus()){
            String phoneNumber = loginRequest.getPhoneNumber();
            contactsService.updateUserStatusAtContacts(phoneNumber,userService.getUserStatus(phoneNumber));
        }
        return response;
    }

    @Override
    public FriendToAddResponse getFriendToAdd(String phoneNumber) throws RemoteException {
        return userService.getFriendToAddData(phoneNumber);
    }

    @Override
    public boolean sendInvitation(String senderPhoneNumber, String receiverPhoneNumber) throws RemoteException {
        return invitationService.sendInvitation(senderPhoneNumber, receiverPhoneNumber);
    }

    @Override
    public int isInvitationSent(String senderPhoneNumber, String receiverPhoneNumber) throws RemoteException {
        return invitationService.isInvitationSent(senderPhoneNumber, receiverPhoneNumber);
    }

    @Override
    public boolean logout(String phoneNumber) throws RemoteException {
        User userInfo = userService.getUserInfo(phoneNumber);
        Mode userMode = userInfo.getMode();
        boolean loggedOut = userService.logoutUser(phoneNumber);
        if(loggedOut && userMode.equals(Mode.ONLINE)){
            contactsService.notifyContacts(phoneNumber, "A contact is offline.", userInfo.getName() + " has become offline");
            contactsService.updateUserModeAtContactsToOffline(phoneNumber);
        }
        return loggedOut;
    }

    @Override
    public void registerConnectedUser(ConnectedUser user) throws RemoteException {
        userService.registerConnectedUser(user);
        User userInfo = userService.getUserInfo(user.getPhoneNumber());
        contactsService.notifyContacts(user.getPhoneNumber(), "A contact is online!", userInfo.getName() + " has become Online!");
    }

    @Override
    public List<ChatMemberDTO> getAllUserChats(String userId) throws RemoteException {
        var service = new UserChatsService();
        return service.getAllChats(userId);
    }

    @Override
    public List<ChatCardsInfoDTO> getUserChatsCardsInfo(String userId) throws RemoteException{
        var service = new UserChatsService();
        List<ChatCardsInfoDTO> chatCardsInfoDTOS = service.getAllChatsInfo(userId);
        return chatCardsInfoDTOS;
    }
    @Override
    public ChatCardsInfoDTO getUserLastChatCardInfo(String userId) throws RemoteException{
        var service = new UserChatsService();
        ChatCardsInfoDTO chatCardInfoDTOS = service.getLastChatInfo(userId);
        return chatCardInfoDTOS;
    }

    @Override
    public void changeProfileAndBio(ImageBioChangeRequest request) throws RemoteException {
        userService.changeProfileAndBio(request);
    }

    @Override
    public void sendMessage(MessageSentDTO message) throws RemoteException {
        MessageDTO storedMessage = messageService.storeMessage(message);
        chatService.sendMessage(storedMessage);
    }

    @Override
    public void sendAttachment(MessageSentDTO message) throws RemoteException{
        Integer attachmentId = attachmentService.storeAttachment(message);
        System.out.println("Attachment id: " + attachmentId);
        message.setAttachmentId(attachmentId);
        MessageDTO storedMessage = messageService.storeMessage(message);
        chatService.sendMessage(storedMessage);
    }

    public byte[] getAttachment(Integer attachmentId) throws RemoteException{
        return attachmentService.getAttachment(attachmentId);
    }

    @Override
    public void prepareCurrentChat(ChatMemberDTO chatMemberDTO) throws RemoteException {
        chatService.prepareCurrentChat(chatMemberDTO);
    }

    @Override
    public List<ContactsDTO> getContacts(String phoneNumber) throws RemoteException {
        return contactService.getContactsDTOList(phoneNumber);
    }

    @Override
    public boolean isPrivateChat(int chatID) throws RemoteException {
        return chatService.isPrivateChat(chatID);
    }

    @Override
    public List<MemberInfoDTO> getAllChatOtherMembersInfo(int chatId, String member) throws RemoteException {
        List<MemberInfoDTO> memberInfoDTOS = chatService.getAllOtherMembersInfo(chatId,member);
        return memberInfoDTOS;
    }


    @Override
    public List<IncomingFriendInvitationResponse> getIncomingFriendRequests(String phoneNumber) throws RemoteException {
        return invitationService.getIncomingFriendRequests(phoneNumber);
    }

    @Override
    public boolean acceptFriendRequest(int invitationId) throws RemoteException {
        return invitationService.acceptFriendRequest(invitationId);
    }

    @Override
    public boolean cancelFriendRequest(int invitationId) throws RemoteException {
        return invitationService.cancelFriendRequest(invitationId);
    }

    @Override
    public boolean areAlreadyFriends(String userPhone, String friendPhone) throws RemoteException {
        return contactsService.areAlreadyFriends(userPhone, friendPhone);
    }

    @Override
    public Mode getUserMode(String phoneNumber) throws RemoteException {
        return userService.getUserMode(phoneNumber);
    }

    @Override
    public Status getUserStatus(String phoneNumber) throws RemoteException {
        return userService.getUserStatus(phoneNumber);
    }

    @Override
    public boolean updateUserModeAndStatus(String phoneNumber,Mode mode, Status status) throws RemoteException {
        boolean modeAndStatusUpdated = userService.updateModeAndStatus(phoneNumber,mode,status);
        if(modeAndStatusUpdated){
            if(mode==Mode.OFFLINE) {
                contactsService.updateUserModeAtContactsToOffline(phoneNumber);
                contactsService.notifyContacts(phoneNumber, "A contact is offline.", phoneNumber + " has become offline");
            }
            else {
                contactsService.updateUserStatusAtContacts(phoneNumber, status);
                contactsService.notifyContacts(phoneNumber, "A contact is online.", phoneNumber + " has become online");
            }
        }
        return modeAndStatusUpdated;
    }

    @Override
    public boolean createGroup(List<ContactsDTO> contactsDTOS, String groupName, String groupDescription, byte[] image) throws RemoteException {
        boolean isSuccessful = groupService.createGroup(contactsDTOS, groupName, groupDescription, image);

        if (isSuccessful) {
            for (ContactsDTO contactsDTO : contactsDTOS) {
                ConnectedUser receiver = Server.getConnectedUsers().get(contactsDTO.getPhoneNumber());
                if (receiver != null) {
                    ChatCardsInfoDTO chatCard = getUserLastChatCardInfo(receiver.getPhoneNumber());
                    receiver.makeNewChatCard(chatCard);
                }
            }
        }

        return isSuccessful;
    }

    @Override
    public void sendPingBack(String phoneNumber) throws RemoteException {
        System.out.println(phoneNumber + " is here");
    }

    @Override
    public List<MessageDTO> getAllChatMessages(int chatID,Integer idLimit) throws RemoteException {
        return messageService.getAllChatMessages(chatID,idLimit);
    }

    @Override
    public boolean updateUserProfile(UpdateUserInfoRequest updateUserInfoRequest) throws RemoteException {
        return userService.updateUser(updateUserInfoRequest);
    }

    @Override
    public boolean updateUserPicture(String phoneNumber, byte[] picture) {
        return userService.updatePicture(phoneNumber, picture);
    }

    @Override
    public boolean updateUserPassword(String phoneNumber, byte[] salt, String password) {
        return userService.updatePassword(phoneNumber, salt, password);
    }

    @Override
    public UserProfileResponse getUserProfile(String phoneNumber) {
        return  userService.getUserProfile(phoneNumber);
    }
}
