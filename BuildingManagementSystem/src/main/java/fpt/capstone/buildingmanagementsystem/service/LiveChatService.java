package fpt.capstone.buildingmanagementsystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.exception.NotFound;
import fpt.capstone.buildingmanagementsystem.exception.ServerError;
import fpt.capstone.buildingmanagementsystem.model.entity.*;
import fpt.capstone.buildingmanagementsystem.model.request.ChatMessageRequest;
import fpt.capstone.buildingmanagementsystem.model.request.ChatMessageRequest2;
import fpt.capstone.buildingmanagementsystem.model.request.CreateChatRequest;
import fpt.capstone.buildingmanagementsystem.model.request.CreateChatRequest2;
import fpt.capstone.buildingmanagementsystem.model.response.*;
import fpt.capstone.buildingmanagementsystem.repository.*;
import fpt.capstone.buildingmanagementsystem.until.Until;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LiveChatService {
    @Autowired
    ChatMessageRepository chatMessageRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ChatUserRepository chatUserRepository;
    @Autowired
    ChatRepository chatRepository;
    @Autowired
    UnReadChatRepository unReadChatRepository;

    @Transactional
    public boolean createChat(CreateChatRequest createChatRequest) {
        try {
            if (createChatRequest.getFrom() != null
                    && createChatRequest.getMessage() != null
                    && createChatRequest.getTo() != null) {
                List<User> to = userRepository.findAllByUserIdIn(createChatRequest.getTo());
                Optional<User> from = userRepository.findByUserId(createChatRequest.getFrom());
                if (to.size() > 0 && from.isPresent()) {
                    Chat chat;
                    List<ChatUser> chatUsers = new ArrayList<>();
                    if (to.size() == 1) {
                        chat = Chat.builder().chatName(null).isGroupChat(false).createAt(Until.generateRealTime()).updateAt(Until.generateRealTime()).build();
                        chatUsers.add(ChatUser.builder().user(to.get(0)).chat(chat).build());
                    } else {
                        chat = Chat.builder().chatName(createChatRequest.getChatName()).isGroupChat(true)
                                .createAt(Until.generateRealTime()).updateAt(Until.generateRealTime()).build();
                        for (User elementTo : to) {
                            chatUsers.add(ChatUser.builder().user(elementTo).chat(chat).build());
                        }
                    }
                    chatUsers.add(ChatUser.builder().user(from.get()).chat(chat).build());
                    chatRepository.saveAndFlush(chat);
                    chatUserRepository.saveAll(chatUsers);
                    ChatMessage chatMessage = ChatMessage.builder()
                            .sender(from.get())
                            .message(createChatRequest.getMessage())
                            .createAt(Until.generateRealTime())
                            .updateAt(Until.generateRealTime())
                            .chat(chat)
                            .type("text")
                            .build();
                    chatMessageRepository.save(chatMessage);
                    List<UnReadChat> list = new ArrayList<>();
                    for (User elementTo : to) {
                        UnReadChat unReadChat = UnReadChat.builder().chat(chat).user(elementTo).build();
                        list.add(unReadChat);
                    }
                    unReadChatRepository.saveAll(list);
                    return true;
                } else {
                    throw new NotFound("requests_fails");
                }
            } else {
                throw new BadRequest("requests_fails");
            }
        } catch (ServerError e) {
            throw new ServerError("fail");
        }
    }

    public boolean newChatMessage(ChatMessageRequest chatMessageRequest) {
        List<User> to = new ArrayList<>();
        Chat chat = chatRepository.findById(chatMessageRequest.getChatId()).get();
        chatUserRepository.findAllByChat_Id(chatMessageRequest.getChatId()).forEach(element -> {
            if (!Objects.equals(element.getUser().getUserId(), chatMessageRequest.getFrom())) {
                to.add(element.getUser());
            }
        });
        Optional<User> from = userRepository.findByUserId(chatMessageRequest.getFrom());
        ChatMessage chatMessage = ChatMessage.builder()
                .sender(from.get())
                .message(chatMessageRequest.getMessage())
                .createAt(Until.generateRealTime())
                .updateAt(Until.generateRealTime())
                .chat(chat)
                .type("text")
                .build();
        chatMessageRepository.saveAndFlush(chatMessage);
        chat.setUpdateAt(Until.generateRealTime());
        chatRepository.saveAndFlush(chat);
        List<UnReadChat> list = new ArrayList<>();
        for (User elementTo : to) {
            if (!unReadChatRepository.existsUnReadChatByChatAndUser(chat, elementTo)) {
                UnReadChat unReadChat = UnReadChat.builder().chat(chat).user(elementTo).build();
                list.add(unReadChat);
            }
        }
        unReadChatRepository.saveAll(list);
        return true;
    }

    public MessageImageAndFileResponse newChatMessage2(String data, MultipartFile file) throws IOException {
        ChatMessageRequest2 chatMessageRequest2 = new ObjectMapper().readValue(data, ChatMessageRequest2.class);
        List<User> to = new ArrayList<>();
        Chat chat = chatRepository.findById(chatMessageRequest2.getChatId()).get();
        chatUserRepository.findAllByChat_Id(chatMessageRequest2.getChatId()).forEach(element -> {
            if (!Objects.equals(element.getUser().getUserId(), chatMessageRequest2.getFrom())) {
                to.add(element.getUser());
            }
        });
        Optional<User> from = userRepository.findByUserId(chatMessageRequest2.getFrom());
        String[] subFileName = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
        List<String> stringList = new ArrayList<>(Arrays.asList(subFileName));
        String name = "livechat_" + UUID.randomUUID() + "." + stringList.get(1);
        Bucket bucket = StorageClient.getInstance().bucket();
        bucket.create(name, file.getBytes(), file.getContentType());

        ChatMessage chatMessage = ChatMessage.builder()
                .sender(from.get())
                .imageName(name).chat(chat)
                .createAt(Until.generateRealTime())
                .updateAt(Until.generateRealTime())
                .type("image")
                .build();
        chatMessageRepository.save(chatMessage);
        chat.setUpdateAt(Until.generateRealTime());
        chatRepository.saveAndFlush(chat);
        List<UnReadChat> list = new ArrayList<>();
        for (User elementTo : to) {
            if (!unReadChatRepository.existsUnReadChatByChatAndUser(chat, elementTo)) {
                UnReadChat unReadChat = UnReadChat.builder().chat(chat).user(elementTo).build();
                list.add(unReadChat);
            }
        }
        unReadChatRepository.saveAll(list);
        return MessageImageAndFileResponse.builder().message(name).build();
    }

    public MessageImageAndFileResponse newChatMessage3(String data, MultipartFile file) throws IOException {
        ChatMessageRequest2 chatMessageRequest2 = new ObjectMapper().readValue(data, ChatMessageRequest2.class);
        List<User> to = new ArrayList<>();
        Chat chat = chatRepository.findById(chatMessageRequest2.getChatId()).get();
        chatUserRepository.findAllByChat_Id(chatMessageRequest2.getChatId()).forEach(element -> {
            if (!Objects.equals(element.getUser().getUserId(), chatMessageRequest2.getFrom())) {
                to.add(element.getUser());
            }
        });
        Optional<User> from = userRepository.findByUserId(chatMessageRequest2.getFrom());
        ChatMessage chatMessage = ChatMessage.builder()
                .sender(from.get())
                .chat(chat)
                .createAt(Until.generateRealTime())
                .updateAt(Until.generateRealTime())
                .fileName(StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename())))
                .fileType(file.getContentType())
                .file(file.getBytes())
                .type("file")
                .build();
        chatMessageRepository.saveAndFlush(chatMessage);
        chat.setUpdateAt(Until.generateRealTime());
        chatRepository.saveAndFlush(chat);
        List<UnReadChat> list = new ArrayList<>();
        for (User elementTo : to) {
            if (!unReadChatRepository.existsUnReadChatByChatAndUser(chat, elementTo)) {
                UnReadChat unReadChat = UnReadChat.builder().chat(chat).user(elementTo).build();
                list.add(unReadChat);
            }
        }
        unReadChatRepository.saveAll(list);
        return MessageImageAndFileResponse.builder().message(StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()))).build();
    }

    public ChatResponse getMessageBySenderAndReceiver(String chatId, String userId) {
        List<MessageResponse> messageResponses = new ArrayList<>();
        List<UserChatResponse> user = new ArrayList<>();
        List<ChatMessage> chatMessages = chatMessageRepository.findAllByChat_Id(chatId);
        chatMessages = chatMessages.stream()
                .sorted((Comparator.comparing(ChatMessage::getCreateAt)))
                .collect(Collectors.toList());
        List<ChatUser> chatUsers = chatUserRepository.findAllByChat_Id(chatId);
        chatMessages.forEach(chatMessage -> {
            boolean myself = false;
            if (chatMessage.getSender().getUserId().equals(userId)) {
                myself = true;
            }
            String message;
            if (chatMessage.getType().equals("image")) {
                message = chatMessage.getImageName();
            } else if (chatMessage.getType().equals("file")) {
                message = chatMessage.getFileName();
            } else {
                message = chatMessage.getMessage();
            }
            MessageResponse messageResponse = new MessageResponse(myself, message, chatMessage.getSender().getUserId(),
                    chatMessage.getCreateAt().toString(), chatMessage.getType());
            messageResponses.add(messageResponse);
        });
        chatUsers.forEach(element -> {
            UserChatResponse userChatResponse = new UserChatResponse(element.getUser().getUserId(), element.getUser().getAccount().getUsername(), element.getUser().getImage());
            user.add(userChatResponse);
        });
        return new ChatResponse(messageResponses, user);
    }

    public List<UserInfoResponse> getAllChatUserSingle(String userId) {
        List<UserInfoResponse> userInfoResponses = new ArrayList<>();
        List<User> userList = userRepository.findAll();
        Optional<User> user = userRepository.findByUserId(userId);
        List<ChatUser> chatUser = chatUserRepository.findAllByUser_UserIdIsNot(userId);
        for (ChatUser userChat : chatUser) {
            if (!userChat.getChat().isGroupChat()) {
                userList.remove(userChat.getUser());
            }
        }
        userList.remove(user.get());
        userList.forEach(element -> {
            UserInfoResponse userInfoResponse = UserInfoResponse.builder().accountId(element.getUserId())
                    .username(element.getAccount().getUsername()).firstName(element.getFirstName()).lastName(element.getLastName()).roleName(element.getAccount().getRole().getRoleName()).build();
            userInfoResponses.add(userInfoResponse);
        });
        return userInfoResponses;
    }

    public List<ListChatResponse> getAllChat(String userId) {
        List<ListChatResponse> listChatResponses = new ArrayList<>();
        List<ChatUser> chatUser = chatUserRepository.findAllByUser_UserId(userId);
        for (ChatUser userChat : chatUser) {
            ListChatResponse listChatResponse = new ListChatResponse();
            List<String> userLists = new ArrayList<>();
            List<String> avatarLists = new ArrayList<>();
            List<ChatUser> chatUsers2 = chatUserRepository.findAllByChat_Id(userChat.getChat().getId());
            listChatResponse.setChatId(userChat.getChat().getId());
            listChatResponse.setUpdateAt(userChat.getChat().getUpdateAt());
            String chatName=userChat.getChat().getChatName();
            for (ChatUser userChat2 : chatUsers2) {
                if (!Objects.equals(userChat2.getUser().getUserId(), userId)) {
                    avatarLists.add(userChat2.getUser().getImage());
                    userLists.add(userChat2.getUser().getUserId());
                }
                if (!userChat2.getChat().isGroupChat()&& !Objects.equals(userChat2.getUser().getUserId(), userId)) {
                    chatName=userChat2.getUser().getAccount().getUsername();
                }
            }
            String isGroup = "false";
            if (userChat.getChat().isGroupChat()) {
                isGroup = "true";
            }
            listChatResponse.setIsGroupChat(isGroup);
            listChatResponse.setAvatar(avatarLists);
            listChatResponse.setUser(userLists);
            listChatResponse.setChatName(chatName);
            listChatResponses.add(listChatResponse);
        }
        listChatResponses = listChatResponses.stream()
                .sorted((Comparator.comparing(ListChatResponse::getUpdateAt).reversed()))
                .collect(Collectors.toList());
        return listChatResponses;
    }
}
