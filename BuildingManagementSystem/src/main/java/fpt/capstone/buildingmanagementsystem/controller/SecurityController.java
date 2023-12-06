package fpt.capstone.buildingmanagementsystem.controller;

import fpt.capstone.buildingmanagementsystem.model.request.ControlLogAndStrangerLogSearchRequest;
import fpt.capstone.buildingmanagementsystem.model.request.RoomBookingRequest;
import fpt.capstone.buildingmanagementsystem.model.response.ControlLogSecurityResponse;
import fpt.capstone.buildingmanagementsystem.model.response.LoadDeviceResponse;
import fpt.capstone.buildingmanagementsystem.model.response.RoomBookingResponse;
import fpt.capstone.buildingmanagementsystem.model.response.StrangerLogSecurityResponse;
import fpt.capstone.buildingmanagementsystem.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@CrossOrigin
public class SecurityController {
    @Autowired
    SecurityService securityService;
    @GetMapping("listAllDevice")
    public List<LoadDeviceResponse> listAllDevice() {
        return securityService.listAllDevice();
    }

    @PostMapping("getListControlLogByDayAndDevice")
    public List<ControlLogSecurityResponse> getListControlLogByDayAndDevice(@RequestBody ControlLogAndStrangerLogSearchRequest controlLogAndStrangerLogSearchRequest) throws ParseException {
        return securityService.getListControlLogByDayAndDevice(controlLogAndStrangerLogSearchRequest);
    }
    @PostMapping("getListStrangerLogByDayAndDevice")
    public List<StrangerLogSecurityResponse> getListStrangerLogByDayAndDevice(@RequestBody ControlLogAndStrangerLogSearchRequest controlLogAndStrangerLogSearchRequest) throws ParseException {
        return securityService.getListStrangerLogByDayAndDevice(controlLogAndStrangerLogSearchRequest);
    }
}
