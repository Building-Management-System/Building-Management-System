package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.model.entity.Room;
import fpt.capstone.buildingmanagementsystem.model.response.RoomResponse;
import fpt.capstone.buildingmanagementsystem.repository.RoomRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.*;

class RoomServiceTest {
    @Mock
    RoomRepository roomRepository;
    @InjectMocks
    RoomService roomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllRoom() {
        when(roomRepository.findAll()).thenReturn(List.of(new Room(0, "roomName", null)));

        List<RoomResponse> result = roomService.getAllRoom();
        Assertions.assertEquals(List.of(new RoomResponse(0, "roomName")), result);
    }

    @Test
    void testGetRoomById() {
        RoomResponse result = roomService.getRoomById(0);
        Assertions.assertEquals(new RoomResponse(0, "roomName"), result);
    }
}
