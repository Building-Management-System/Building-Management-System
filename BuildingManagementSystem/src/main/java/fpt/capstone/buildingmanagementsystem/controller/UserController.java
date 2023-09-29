package fpt.capstone.buildingmanagementsystem.controller;

import fpt.capstone.buildingmanagementsystem.model.request.ChangeRoleRequest;
import fpt.capstone.buildingmanagementsystem.model.request.ChangeUserInfoRequest;
import fpt.capstone.buildingmanagementsystem.service.UserManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class UserController {
    @Autowired
    UserManageService userManageService;
    @RequestMapping(value = "/changeUserInfo", method = RequestMethod.POST)
    public boolean changeUserInfo(@RequestBody ChangeUserInfoRequest changeUserInfoRequest) throws Exception {
        return userManageService.ChangeUserInfo(changeUserInfoRequest);
    }
}
