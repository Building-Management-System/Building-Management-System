package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.exception.NotFound;
import fpt.capstone.buildingmanagementsystem.exception.ServerError;
import fpt.capstone.buildingmanagementsystem.mapper.UserMapper;
import fpt.capstone.buildingmanagementsystem.model.entity.User;
import fpt.capstone.buildingmanagementsystem.model.request.ChangeUserInfoRequest;
import fpt.capstone.buildingmanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static fpt.capstone.buildingmanagementsystem.until.Until.generateRealTime;

@Service
public class UserManageService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserMapper userMapper;
    public boolean ChangeUserInfo(ChangeUserInfoRequest changeUserInfoRequest) {
        try {
            String userId=changeUserInfoRequest.getUser_id();
            if (userId != null && changeUserInfoRequest.getFirst_name()!=null &&
                    changeUserInfoRequest.getLast_name()!=null &&
                    changeUserInfoRequest.getCountry()!=null &&
                    changeUserInfoRequest.getEmail()!=null &&
                    changeUserInfoRequest.getGender()!=null &&
                    changeUserInfoRequest.getCity()!=null &&
                    changeUserInfoRequest.getTelephone_number()!=null &&
                    changeUserInfoRequest.getDate_of_birth()!=null
            ) {
                if (!userRepository.existsById(userId)) {
                    throw new NotFound("user_not_found");
                }
                userRepository.updateUserInfo(changeUserInfoRequest.getFirst_name(),
                        changeUserInfoRequest.getLast_name(), changeUserInfoRequest.getGender(), changeUserInfoRequest.getDate_of_birth()
                        ,changeUserInfoRequest.getTelephone_number(),changeUserInfoRequest.getCountry()
                        ,changeUserInfoRequest.getCity(),changeUserInfoRequest.getEmail(),generateRealTime(),changeUserInfoRequest.getUser_id());
                return true;
            } else {
                throw new BadRequest("request_fail");
            }
        } catch (ServerError e) {
            throw new ServerError("fail");
        }
    }
}
