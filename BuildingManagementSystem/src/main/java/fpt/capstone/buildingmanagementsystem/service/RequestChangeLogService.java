package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.exception.Conflict;
import fpt.capstone.buildingmanagementsystem.exception.ServerError;
import fpt.capstone.buildingmanagementsystem.mapper.ChangeLogMapper;
import fpt.capstone.buildingmanagementsystem.model.entity.ChangeLog;
import fpt.capstone.buildingmanagementsystem.model.entity.User;
import fpt.capstone.buildingmanagementsystem.model.request.SaveChangeLogRequest;
import fpt.capstone.buildingmanagementsystem.repository.ChangeLogRepository;
import fpt.capstone.buildingmanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Optional;

import static fpt.capstone.buildingmanagementsystem.validate.Validate.validateStartTimeAndEndTime;

@Service
public class RequestChangeLogService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ChangeLogRepository changeLogRepository;
    @Autowired
    ChangeLogMapper changeLogMapper;

    public boolean saveChangeLog(SaveChangeLogRequest saveChangeLogRequest) {
        try {
            if (saveChangeLogRequest.getManagerId() != null &&
                    saveChangeLogRequest.getEmployeeId() != null &&
                    saveChangeLogRequest.getDate() != null &&
                    saveChangeLogRequest.getChangeType() != null
            ) {
                ChangeLog changeLog = changeLogMapper.convert(saveChangeLogRequest);
                if (validateStartTimeAndEndTime(saveChangeLogRequest.getManualCheckIn(), saveChangeLogRequest.getManualCheckOut())) {
                    Optional<User> userEmp = userRepository.findByUserId(saveChangeLogRequest.getEmployeeId());
                    Optional<User> userManager = userRepository.findByUserId(saveChangeLogRequest.getManagerId());
                    if (userEmp.isPresent() && userManager.isPresent()) {
                        changeLog.setEmployee(userEmp.get());
                        changeLog.setManager(userManager.get());
                        changeLogRepository.save(changeLog);
                        return true;
                    } else {
                        throw new BadRequest("request_fail");
                    }
                } else {
                    throw new Conflict("checkout_time_need_to_be_larger_checkin");
                }
            } else {
                throw new BadRequest("request_fail");
            }
        } catch (ServerError | ParseException e) {
            throw new ServerError("fail");
        }
    }
}
