package fpt.capstone.buildingmanagementsystem.controller;

import fpt.capstone.buildingmanagementsystem.model.request.ChatMessageRequest;
import fpt.capstone.buildingmanagementsystem.model.response.MessageResponse;
import fpt.capstone.buildingmanagementsystem.service.LiveChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class LiveChatController {

    @Autowired
    LiveChatService liveChatService;

    @PostMapping("/chat")
    public ResponseEntity<?> createNewChat(@RequestBody ChatMessageRequest chatMessageRequest) {
        return liveChatService.createChat(chatMessageRequest);
    }

    @GetMapping("/message")
    public List<MessageResponse> getMessagesFromTo(@Param("from") String from, @Param("to") String to) {
        return liveChatService.getMessageBySenderAndReceiver(from, to);
    }

}
