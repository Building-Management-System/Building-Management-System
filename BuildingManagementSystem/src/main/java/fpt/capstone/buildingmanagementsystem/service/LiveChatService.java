package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.model.entity.ChatMessage;
import fpt.capstone.buildingmanagementsystem.model.entity.User;
import fpt.capstone.buildingmanagementsystem.model.request.ChatMessageRequest;
import fpt.capstone.buildingmanagementsystem.model.response.ChatMessageResponse;
import fpt.capstone.buildingmanagementsystem.repository.ChatMessageRepository;
import fpt.capstone.buildingmanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

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
                    .createAt(Instant.now())
                    .updateAt(Instant.now())
                    .build();

            chatMessageRepository.save(chatMessage);
            return ResponseEntity.ok().body(new ChatMessageResponse(
                    List.of(from.getUserId(), to.getUserId()),
                    from.getUserId(),
                    to.getUserId(),
                    chatMessage.getMessage(),
                    chatMessage.getCreateAt(),
                    chatMessage.getUpdateAt()
            ));
        } catch (Exception e) {
            throw new BadRequest("Could not found user");
        }
    }


}
