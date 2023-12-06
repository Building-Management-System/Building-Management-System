package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.model.entity.*;
import fpt.capstone.buildingmanagementsystem.model.request.ControlLogAndStrangerLogSearchRequest;
import fpt.capstone.buildingmanagementsystem.model.response.ControlLogSecurityResponse;
import fpt.capstone.buildingmanagementsystem.model.response.LoadDeviceResponse;
import fpt.capstone.buildingmanagementsystem.model.response.StrangerLogSecurityResponse;
import fpt.capstone.buildingmanagementsystem.repository.*;
import fpt.capstone.buildingmanagementsystem.validate.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SecurityService {
    @Autowired
    ControlLogLcdRepository controlLogLcdRepository;
    @Autowired
    StrangerLogLcdRepository strangerLogLcdRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    DeviceRepository deviceRepository;
    public List<LoadDeviceResponse> listAllDevice(){
        List<LoadDeviceResponse> loadDeviceResponseList= new ArrayList<>();
        deviceRepository.findAll().forEach(device -> {
            LoadDeviceResponse loadDeviceResponse= LoadDeviceResponse.builder().deviceId(device.getId()).deviceName(device.getDeviceName()).build();
            loadDeviceResponseList.add(loadDeviceResponse);
        });
        return loadDeviceResponseList;
    }
    public List<ControlLogSecurityResponse> getListControlLogByDayAndDevice(ControlLogAndStrangerLogSearchRequest controlLogAndStrangerLogSearchRequest) throws ParseException {
        List<ControlLogSecurityResponse> controlLogSecurityResponseList= new ArrayList<>();
        String startTimeRequest=controlLogAndStrangerLogSearchRequest.getStartTime();
        String endTimeRequest=controlLogAndStrangerLogSearchRequest.getEndTime();
        String date= controlLogAndStrangerLogSearchRequest.getDate();
        if(Validate.validateStartTimeAndEndTime(startTimeRequest,endTimeRequest)) {
            Date startDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date+" "+startTimeRequest);
            Date endDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date+" "+endTimeRequest);
            List<ControlLogLcd> controlLogLcdList = controlLogLcdRepository.getControlLogLcdListByDateAndDevice(
                    startDate, endDate, controlLogAndStrangerLogSearchRequest.getDeviceId());
            controlLogLcdList.forEach(element -> {
                User user= accountRepository.findByUsername(element.getPersionName()).get().getUser();
                Room room=roomRepository.findByDevice(element.getDevice());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = dateFormat.format(element.getTime());
                ControlLogSecurityResponse controlLogSecurityResponse= ControlLogSecurityResponse.builder().image(element.getPic()).timeRecord(formattedDate)
                        .username(element.getPersionName()).lastName(user.getLastName()).firstName(user.getFirstName()).department(user.getDepartment().getDepartmentName())
                        .room(room.getRoomName()).verifyType(element.getStatus().toString()).build();
                controlLogSecurityResponseList.add(controlLogSecurityResponse);
            });
            return controlLogSecurityResponseList;
        }else {
            throw new BadRequest("end_date_must_be_greater_than_start_date");
        }
    }
    public List<StrangerLogSecurityResponse> getListStrangerLogByDayAndDevice(ControlLogAndStrangerLogSearchRequest controlLogAndStrangerLogSearchRequest) throws ParseException {
        List<StrangerLogSecurityResponse> strangerLogSecurityResponses= new ArrayList<>();
        String startTimeRequest=controlLogAndStrangerLogSearchRequest.getStartTime();
        String endTimeRequest=controlLogAndStrangerLogSearchRequest.getEndTime();
        String date= controlLogAndStrangerLogSearchRequest.getDate();
        if(Validate.validateStartTimeAndEndTime(startTimeRequest,endTimeRequest)) {
            Date startDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date+" "+startTimeRequest);
            Date endDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date+" "+endTimeRequest);
            List<StrangerLogLcd> strangerLogLcdList = strangerLogLcdRepository.getStrangerLogLcdListByDateAndDevice(
                    startDate, endDate, controlLogAndStrangerLogSearchRequest.getDeviceId());
            strangerLogLcdList.forEach(element -> {
                Room room=roomRepository.findByDevice(element.getDevice());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = dateFormat.format(element.getTime());
                StrangerLogSecurityResponse strangerLogSecurityResponse= StrangerLogSecurityResponse.builder().snapId(element.getSnapId()).time(formattedDate)
                        .deviceName(element.getDevice().getDeviceName()).deviceId(element.getDevice().getDeviceId()).image(element.getImage())
                        .temperature(element.getTemperature()).room(room.getRoomName()).build();
                strangerLogSecurityResponses.add(strangerLogSecurityResponse);
            });
            return strangerLogSecurityResponses;
        }else {
            throw new BadRequest("end_date_must_be_greater_than_start_date");
        }
    }
}
