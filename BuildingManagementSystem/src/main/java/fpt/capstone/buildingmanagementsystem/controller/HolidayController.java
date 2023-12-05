package fpt.capstone.buildingmanagementsystem.controller;

import fpt.capstone.buildingmanagementsystem.model.entity.Holiday;
import fpt.capstone.buildingmanagementsystem.model.request.HolidayDeleteRequest;
import fpt.capstone.buildingmanagementsystem.model.request.HolidaySaveRequest;
import fpt.capstone.buildingmanagementsystem.model.response.ChatResponse;
import fpt.capstone.buildingmanagementsystem.model.response.HolidayResponse;
import fpt.capstone.buildingmanagementsystem.service.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@CrossOrigin
public class HolidayController {
    @Autowired
    HolidayService holidayService;
    @PostMapping("saveHoliday")
    public boolean saveHoliday(@RequestBody HolidaySaveRequest holidaySaveRequest) throws ParseException {
        return holidayService.saveHoliday(holidaySaveRequest);
    }
    @PostMapping("deleteHoliday")
    public boolean deleteHoliday(@RequestBody HolidayDeleteRequest holidayDeleteRequest) {
        return holidayService.deleteHoliday(holidayDeleteRequest);
    }
    @GetMapping("/listAllHoliday")
    public List<HolidayResponse> listAllHoliday() {
        return holidayService.listAllHoliday();
    }
}
