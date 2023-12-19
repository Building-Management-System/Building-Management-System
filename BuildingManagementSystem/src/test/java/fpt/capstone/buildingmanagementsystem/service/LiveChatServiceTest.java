package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.model.entity.*;
import fpt.capstone.buildingmanagementsystem.model.request.*;
import fpt.capstone.buildingmanagementsystem.model.response.*;
import fpt.capstone.buildingmanagementsystem.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.mockito.Mockito.*;

class LiveChatServiceTest {
    @Mock
    ChatMessageRepository chatMessageRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    ChatUserRepository chatUserRepository;
    @Mock
    ChatRepository chatRepository;
    @Mock
    UnReadChatRepository unReadChatRepository;
    @InjectMocks
    LiveChatService liveChatService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateChat() {
        when(userRepository.findByUserId(anyString())).thenReturn(null);
        when(userRepository.findAllByUserIdIn(any())).thenReturn(List.of(new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), null, null)));

        ListChatResponse result = liveChatService.createChat(new CreateChatRequest("from", "chatName", List.of("String"), "message"));
        Assertions.assertEquals(new ListChatResponse("chatId", "chatName", "isGroupChat", List.of("String"), List.of(new UserInfoResponse("accountId", "username", "firstName", "lastName", "image", "roleName")), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), "read", "admin"), result);
    }

    @Test
    void testNewChatMessage() {
        when(userRepository.findByUserId(anyString())).thenReturn(null);
        when(chatUserRepository.findAllByChat_Id(anyString())).thenReturn(List.of(new ChatUser("id", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), null, null), new Chat("id", "chatName", true, new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), "createdBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime()))));
        when(unReadChatRepository.existsUnReadChatByChatAndUser(any(), any())).thenReturn(true);

        MessageImageAndFileResponse result = liveChatService.newChatMessage(new ChatMessageRequest("from", "chatId", "message"));
        Assertions.assertEquals(new MessageImageAndFileResponse("messageId", "message", "senderId"), result);
    }

    @Test
    void testNewChatMessage2() throws IOException {
        when(userRepository.findByUserId(anyString())).thenReturn(null);
        when(chatUserRepository.findAllByChat_Id(anyString())).thenReturn(List.of(new ChatUser("id", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), null, null), new Chat("id", "chatName", true, new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), "createdBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime()))));
        when(unReadChatRepository.existsUnReadChatByChatAndUser(any(), any())).thenReturn(true);

        MessageImageAndFileResponse result = liveChatService.newChatMessage2("data", null);
        Assertions.assertEquals(new MessageImageAndFileResponse("messageId", "message", "senderId"), result);
    }

    @Test
    void testNewChatMessage3() throws IOException {
        when(userRepository.findByUserId(anyString())).thenReturn(null);
        when(chatUserRepository.findAllByChat_Id(anyString())).thenReturn(List.of(new ChatUser("id", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), null, null), new Chat("id", "chatName", true, new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), "createdBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime()))));
        when(unReadChatRepository.existsUnReadChatByChatAndUser(any(), any())).thenReturn(true);

        MessageImageAndFileResponse result = liveChatService.newChatMessage3("data", null);
        Assertions.assertEquals(new MessageImageAndFileResponse("messageId", "message", "senderId"), result);
    }

    @Test
    void testGetMessageBySenderAndReceiver() {
        when(chatMessageRepository.findAllByChat_Id(anyString())).thenReturn(List.of(new ChatMessage("id", "message", "fileName", "imageName", new byte[]{(byte) 0}, "fileType", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), "type", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), null, null), new Chat("id", "chatName", true, new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), "createdBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime()))));
        when(userRepository.findAllByUserIdIn(any())).thenReturn(List.of(new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), null, null)));
        when(chatUserRepository.findAllByChat_Id(anyString())).thenReturn(List.of(new ChatUser("id", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), null, null), new Chat("id", "chatName", true, new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), "createdBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime()))));
        when(chatUserRepository.existsByUserAndChat(any(), any())).thenReturn(true);

        ChatResponse result = liveChatService.getMessageBySenderAndReceiver("chatId", "userId");
        Assertions.assertEquals(new ChatResponse(List.of(new MessageResponse("messageId", true, "message", "senderId", "createdAt", "type")), List.of(new UserChatResponse("userId", "username", "image")), "admin"), result);
    }

    @Test
    void testGetAllChatUserSingle() {
        when(userRepository.findByUserId(anyString())).thenReturn(null);
        when(userRepository.findAllByAccount_Status_StatusName(anyString())).thenReturn(List.of(new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), null, null)));
        when(chatUserRepository.find(anyString(), anyString())).thenReturn(List.of(new ChatUser("id", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), null, null), new Chat("id", "chatName", true, new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), "createdBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime()))));
        when(chatUserRepository.findAllByUser_UserId(anyString())).thenReturn(List.of(new ChatUser("id", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), null, null), new Chat("id", "chatName", true, new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), "createdBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime()))));

        List<UserInfoResponse> result = liveChatService.getAllChatUserSingle("userId");
        Assertions.assertEquals(List.of(new UserInfoResponse("accountId", "username", "firstName", "lastName", "image", "roleName")), result);
    }

    @Test
    void testGetAllChat() {
        when(chatUserRepository.findAllByChat_Id(anyString())).thenReturn(List.of(new ChatUser("id", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), null, null), new Chat("id", "chatName", true, new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), "createdBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime()))));
        when(chatUserRepository.findAllByUser_UserId(anyString())).thenReturn(List.of(new ChatUser("id", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), null, null), new Chat("id", "chatName", true, new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), "createdBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime()))));
        when(unReadChatRepository.existsUnReadChatByChatAndUser(any(), any())).thenReturn(true);

        List<ListChatResponse> result = liveChatService.getAllChat("userId");
        Assertions.assertEquals(List.of(new ListChatResponse("chatId", "chatName", "isGroupChat", List.of("String"), List.of(new UserInfoResponse("accountId", "username", "firstName", "lastName", "image", "roleName")), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), "read", "admin")), result);
    }

    @Test
    void testUpdateChat() {
        when(userRepository.findByUserId(anyString())).thenReturn(null);
        when(userRepository.findAllByUserIdIn(any())).thenReturn(List.of(new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), null, null)));
        when(chatUserRepository.findAllByChat_Id(anyString())).thenReturn(List.of(new ChatUser("id", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), null, null), new Chat("id", "chatName", true, new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), "createdBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime()))));
        when(unReadChatRepository.existsUnReadChatByChatAndUser(any(), any())).thenReturn(true);
        when(unReadChatRepository.findAllByChat_Id(anyString())).thenReturn(List.of(new UnReadChat("id", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), null, null), new Chat("id", "chatName", true, new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), "createdBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime()))));

        ListChatResponse result = liveChatService.updateChat(new UpdateGroupChatRequest("chatId", "isGroup", "chatName", List.of("String")));
        Assertions.assertEquals(new ListChatResponse("chatId", "chatName", "isGroupChat", List.of("String"), List.of(new UserInfoResponse("accountId", "username", "firstName", "lastName", "image", "roleName")), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 40).getTime(), "read", "admin"), result);
    }

    @Test
    void testRemoveFromChat() {
        when(userRepository.findByUserId(anyString())).thenReturn(null);

        boolean result = liveChatService.removeFromChat(new RemoveUserAndChangeAdminRequest("chatId", "userId"));
        Assertions.assertEquals(true, result);
    }

    @Test
    void testReadChat() {
        when(userRepository.findByUserId(anyString())).thenReturn(null);

        boolean result = liveChatService.readChat(new RemoveUserAndChangeAdminRequest("chatId", "userId"));
        Assertions.assertEquals(true, result);
    }

    @Test
    void testUpdateChange() {
        boolean result = liveChatService.updateChange(new RemoveUserAndChangeAdminRequest("chatId", "userId"));
        Assertions.assertEquals(true, result);
    }

    @Test
    void testGetFileChatDownload() {
        when(chatMessageRepository.findByIdAndFileName(anyString(), anyString())).thenReturn(null);

        FileDataResponse result = liveChatService.getFileChatDownload(new FileRequest("messageId", "fileName"));
        Assertions.assertEquals(new FileDataResponse("fileId", "name", "type", new byte[]{(byte) 0}), result);
    }
}
