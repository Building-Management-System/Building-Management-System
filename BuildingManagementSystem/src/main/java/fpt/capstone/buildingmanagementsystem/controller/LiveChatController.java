package fpt.capstone.buildingmanagementsystem.controller;

import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.exception.ServerError;
import fpt.capstone.buildingmanagementsystem.model.request.ChatMessageRequest;
import fpt.capstone.buildingmanagementsystem.model.request.CreateChatRequest;
import fpt.capstone.buildingmanagementsystem.model.response.ChatResponse;
import fpt.capstone.buildingmanagementsystem.model.response.MessageResponse;
import fpt.capstone.buildingmanagementsystem.service.LiveChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
public class LiveChatController {

    @Autowired
    LiveChatService liveChatService;

    @PostMapping("/createNewChat")
    public boolean createNewChat(@RequestBody CreateChatRequest createChatRequest) {
        return liveChatService.createChat(createChatRequest);
    }
    @PostMapping("/createNewMessage")
    public boolean createNewMessage(@RequestBody ChatMessageRequest chatMessageRequest) {
        return liveChatService.newChatMessage(chatMessageRequest);
    }
    @RequestMapping(path = "/createNewChat2", method = RequestMethod.POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public boolean createNewChat2(@RequestParam("data") String data,@RequestParam("image") MultipartFile image) {
        return liveChatService.createChat2(data,image);
    }
    @RequestMapping(path ="/createNewMessage2", method = RequestMethod.POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public boolean createNewMessage2(@RequestParam("data") String data,@RequestParam("image") MultipartFile image) throws IOException {
        return liveChatService.newChatMessage2(data,image);
    }
    @RequestMapping(path = "/createNewChat3", method = RequestMethod.POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public boolean createNewChat3(@RequestParam("data") String data,@RequestParam("file") MultipartFile file) {
        return liveChatService.createChat3(data,file);
    }
    @RequestMapping(path ="/createNewMessage3", method = RequestMethod.POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public boolean createNewMessage3(@RequestParam("data") String data,@RequestParam("file") MultipartFile file) throws IOException {
        return liveChatService.newChatMessage3(data,file);
    }
    @GetMapping("/message")
    public ChatResponse getMessagesByChatId(@Param("chatId") String chatId, @Param("userId") String userId) {
        return liveChatService.getMessageBySenderAndReceiver(chatId,userId);
    }

}
