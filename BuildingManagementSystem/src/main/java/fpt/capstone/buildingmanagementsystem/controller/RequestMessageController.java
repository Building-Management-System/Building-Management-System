package fpt.capstone.buildingmanagementsystem.controller;

import fpt.capstone.buildingmanagementsystem.model.response.RequestMessageResponse;
import fpt.capstone.buildingmanagementsystem.service.RequestMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class RequestMessageController {

    @Autowired
    RequestMessageService requestMessageService;

    @GetMapping("getAttendanceMessage")
    public List<Map<RequestMessageResponse, Object>> getAllAttendanceMessageByRequestId(@RequestParam("request_id") String requestId) {
        return requestMessageService.getAllAttendanceMessageByRequestId(requestId);
    }

    @GetMapping("getRoomBookingMessage")
    public List<Map<RequestMessageResponse, Object>> getAllRoomBookingMessageByRequestId(@RequestParam("request_id") String requestId) {
        return requestMessageService.getAllRoomBookingMessageByRequestId(requestId);
    }
}
