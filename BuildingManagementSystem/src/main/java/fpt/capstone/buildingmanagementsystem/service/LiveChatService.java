package fpt.capstone.buildingmanagementsystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.exception.ServerError;
import fpt.capstone.buildingmanagementsystem.model.entity.ChatMessage;
import fpt.capstone.buildingmanagementsystem.model.entity.User;
import fpt.capstone.buildingmanagementsystem.model.request.ChatMessageRequest;
import fpt.capstone.buildingmanagementsystem.model.request.ChatMessageRequest2;
import fpt.capstone.buildingmanagementsystem.model.response.ChatMessageResponse;
import fpt.capstone.buildingmanagementsystem.model.response.MessageResponse;
import fpt.capstone.buildingmanagementsystem.repository.ChatMessageRepository;
import fpt.capstone.buildingmanagementsystem.repository.UserRepository;
import fpt.capstone.buildingmanagementsystem.until.Until;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class LiveChatService {

    @Autowired
    ChatMessageRepository chatMessageRepository;

    @Autowired
    UserRepository userRepository;

    public ResponseEntity<?> createChat(ChatMessageRequest chatMessageRequest) {
        try {
            User from = userRepository.findByUserId(chatMessageRequest.getFrom())
                    .orElseThrow(Exception::new);
            User to = userRepository.findByUserId(chatMessageRequest.getTo())
                    .orElseThrow(Exception::new);

            ChatMessage chatMessage = ChatMessage.builder()
                    .sender(from)
                    .receiver(to)
                    .message(chatMessageRequest.getMessage())
                    .createAt(Until.generateRealTime())
                    .updateAt(Until.generateRealTime())
                    .type("text")
                    .build();

            chatMessageRepository.save(chatMessage);
            return ResponseEntity.ok().body(new ChatMessageResponse(
                    List.of(from.getUserId(), to.getUserId()),
                    from.getUserId(),
                    to.getUserId(),
                    chatMessage.getMessage(), "text",
                    chatMessage.getCreateAt().toString(),
                    chatMessage.getUpdateAt().toString()
            ));
        } catch (Exception e) {
            throw new BadRequest("Could not found user");
        }
    }

    public ResponseEntity<?> createChat2(String data, MultipartFile file) {
        try {
            ChatMessageRequest2 chatMessageRequest2 = new ObjectMapper().readValue(data, ChatMessageRequest2.class);
            if (chatMessageRequest2.getFrom() != null && chatMessageRequest2.getTo() != null && file != null) {
                User from = userRepository.findByUserId(chatMessageRequest2.getFrom())
                        .orElseThrow(Exception::new);
                User to = userRepository.findByUserId(chatMessageRequest2.getTo())
                        .orElseThrow(Exception::new);

                String[] subFileName = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
                List<String> stringList = new ArrayList<>(Arrays.asList(subFileName));
                String name = "livechat_" + UUID.randomUUID() + "." + stringList.get(1);
                Bucket bucket = StorageClient.getInstance().bucket();
                bucket.create(name, file.getBytes(), file.getContentType());

                ChatMessage chatMessage = ChatMessage.builder()
                        .sender(from)
                        .receiver(to)
                        .message(name)
                        .createAt(Until.generateRealTime())
                        .updateAt(Until.generateRealTime())
                        .type("image")
                        .build();
                chatMessageRepository.save(chatMessage);
                return ResponseEntity.ok().body(new ChatMessageResponse(
                        List.of(from.getUserId(), to.getUserId()),
                        from.getUserId(),
                        to.getUserId(),
                        name, "image",
                        chatMessage.getCreateAt().toString(),
                        chatMessage.getUpdateAt().toString()
                ));
            } else {
                throw new BadRequest("request_fail");
            }
        } catch (Exception e) {
            throw new ServerError("fail");
        }
    }

    public ResponseEntity<?> createChat3(String data, MultipartFile file) {
        try {
            ChatMessageRequest2 chatMessageRequest2 = new ObjectMapper().readValue(data, ChatMessageRequest2.class);
            if (chatMessageRequest2.getFrom() != null && chatMessageRequest2.getTo() != null && file != null) {
                if (file.getSize() > 10485760) {
                    User from = userRepository.findByUserId(chatMessageRequest2.getFrom())
                            .orElseThrow(Exception::new);
                    User to = userRepository.findByUserId(chatMessageRequest2.getTo())
                            .orElseThrow(Exception::new);

                    String[] subFileName = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
                    List<String> stringList = new ArrayList<>(Arrays.asList(subFileName));
                    String name = "livechat_" + UUID.randomUUID() + "." + stringList.get(1);
                    Bucket bucket = StorageClient.getInstance().bucket();
                    bucket.create(name, file.getBytes(), file.getContentType());

                    ChatMessage chatMessage = ChatMessage.builder()
                            .sender(from)
                            .receiver(to)
                            .message(name)
                            .createAt(Until.generateRealTime())
                            .updateAt(Until.generateRealTime())
                            .type("file")
                            .build();
                    chatMessageRepository.save(chatMessage);
                    return ResponseEntity.ok().body(new ChatMessageResponse(
                            List.of(from.getUserId(), to.getUserId()),
                            from.getUserId(),
                            to.getUserId(),
                            name, "file",
                            chatMessage.getCreateAt().toString(),
                            chatMessage.getUpdateAt().toString()
                    ));
                } else {
                    throw new BadRequest("file_oversize");
                }
            } else {
                throw new BadRequest("request_fail");
            }
        } catch (Exception e) {
            throw new ServerError("fail");
        }
    }

    public List<MessageResponse> getMessageBySenderAndReceiver(String from, String to) {

        List<MessageResponse> messageResponses = new ArrayList<>();

        List<ChatMessage> chatMessages = chatMessageRepository.findBySenderIdAndReceiverId(from, to);

        chatMessages.forEach(chatMessage -> {
            MessageResponse messageResponse;
            if (chatMessage.getSender().getUserId().equals(from)) {
                messageResponse = new MessageResponse(true, chatMessage.getMessage(), chatMessage.getCreateAt().toString(), chatMessage.getType());
            } else {
                messageResponse = new MessageResponse(false, chatMessage.getMessage(), chatMessage.getCreateAt().toString(), chatMessage.getType());
            }
            messageResponses.add(messageResponse);
        });
        return messageResponses;
    }

}
