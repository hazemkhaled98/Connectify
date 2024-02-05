package com.connectify.services;

import com.connectify.dto.MessageSentDTO;
import com.connectify.model.dao.AttachmentDAO;
import com.connectify.model.dao.impl.AttachmentsDAOImpl;
import com.connectify.model.entities.Attachments;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.*;
import java.util.concurrent.ExecutionException;


public class AttachmentService {

    private final ExecutorService executor;

    public AttachmentService() {
        executor = Executors.newCachedThreadPool();
    }

    public Integer storeAttachment(MessageSentDTO message){
        Callable<Integer> callable = () -> {
            Path attachmentsPath = Paths.get(getClass().getClassLoader().getResource("attachments").toURI());
            Path chatFolderPath = attachmentsPath.resolve(String.valueOf(message.getChatId()));
            if (!Files.exists(chatFolderPath)) {
                Files.createDirectories(chatFolderPath);
            }

            Path senderFolderPath = chatFolderPath.resolve(message.getSender());
            if (!Files.exists(senderFolderPath)) {
                Files.createDirectories(senderFolderPath);
            }

            Path attachmentPath = message.getAttachment().toPath();
            if (Files.exists(attachmentPath)) {
                Path newFilePath = senderFolderPath.resolve(attachmentPath.getFileName());
                Files.copy(attachmentPath, newFilePath, StandardCopyOption.REPLACE_EXISTING);

                Attachments attachment = new Attachments();
                attachment.setName(newFilePath.toString());
                attachment.setExtension(getExtension(newFilePath.toString()));
                attachment.setSize((int) Files.size(newFilePath));
                AttachmentDAO attachmentDAO = new AttachmentsDAOImpl();
                return attachmentDAO.insertAndReturnID(attachment);
            }
            return null;
        };

        try {
            return executor.submit(callable).get();
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Exception: " + e.getMessage());
            return null;
        }
    }

    public File getAttachment(Integer attachmentId) {
        AttachmentDAO attachmentDAO = new AttachmentsDAOImpl();
        Attachments attachment = attachmentDAO.get(attachmentId);
        if (attachment != null) {
            Path filePath = Paths.get(attachment.getName());
            return filePath.toFile();
        }
        return null;
    }


    private String getExtension(String path) {
        int index = path.lastIndexOf('.');
        return path.substring(index + 1);
    }


}
