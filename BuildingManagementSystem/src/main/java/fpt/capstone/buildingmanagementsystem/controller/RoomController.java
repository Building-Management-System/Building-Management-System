package fpt.capstone.buildingmanagementsystem.controller;

import fpt.capstone.buildingmanagementsystem.model.request.RoomBookingIdRequest;
import fpt.capstone.buildingmanagementsystem.model.response.RoomBookingResponse;
import fpt.capstone.buildingmanagementsystem.model.response.RoomResponse;
import fpt.capstone.buildingmanagementsystem.service.RoomBookingService;
import fpt.capstone.buildingmanagementsystem.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class RoomController {
    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomBookingService roomBookingService;

    @GetMapping("getAllRooms")
    public List<RoomResponse> getAllRoom() {
        return roomService.getAllRoom();
    }

    @GetMapping("getRoomById")
    public ResponseEntity<?> getRoomById(@RequestParam("room_id") int roomId) {
        return ResponseEntity.ok(roomService.getRoomById(roomId));
    }

    @GetMapping("getBookedRoom")
    public List<RoomBookingResponse> getAllBookedRoom() {
        return roomBookingService.getAllBookedRoom();
    }

    @PostMapping("acceptBookRoom")
    public boolean acceptBookRoom(@RequestBody RoomBookingIdRequest roomBookingFormRoomId) {
        return roomBookingService.acceptBooking(roomBookingFormRoomId.getRoomBookingFormRoomId());
    }

    @PostMapping("acceptBookRoom")
    public boolean rejectBookRoom(@RequestBody RoomBookingIdRequest roomBookingFormRoomId) {
        return roomBookingService.rejectRoomBooking(roomBookingFormRoomId.getRoomBookingFormRoomId());
    }
}
