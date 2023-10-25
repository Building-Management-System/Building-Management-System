package fpt.capstone.buildingmanagementsystem.controller;

import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.exception.ServerError;
import fpt.capstone.buildingmanagementsystem.model.request.ChatMessageRequest;
import fpt.capstone.buildingmanagementsystem.model.response.MessageResponse;
import fpt.capstone.buildingmanagementsystem.service.LiveChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

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
    @RequestMapping(path = "/chat2", method = RequestMethod.POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> createNewChat2(@RequestParam("data") String data,@RequestParam("message") MultipartFile image) {
        return liveChatService.createChat2(data,image);
    }
    @RequestMapping(path = "/chat3", method = RequestMethod.POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> createNewChat3(@RequestParam("data") String data,@RequestParam("message") MultipartFile file){
        try{
            return liveChatService.createChat3(data,file);
        }catch (MaxUploadSizeExceededException e){
            throw new ServerError("file_oversize");
        }
    }
    @GetMapping("/message")
    public List<MessageResponse> getMessagesFromTo(@Param("from") String from, @Param("to") String to) {
        return liveChatService.getMessageBySenderAndReceiver(from, to);
    }

}
