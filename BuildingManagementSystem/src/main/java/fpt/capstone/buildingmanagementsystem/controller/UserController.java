package fpt.capstone.buildingmanagementsystem.controller;

import fpt.capstone.buildingmanagementsystem.model.request.AcceptChangeUserInfo;
import fpt.capstone.buildingmanagementsystem.model.request.GetUserInfoRequest;
import fpt.capstone.buildingmanagementsystem.service.UserManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
public class UserController {
    @Autowired
    UserManageService userManageService;
    @RequestMapping(value = "/getAllUserInfoPending", method = RequestMethod.GET)
    public ResponseEntity<?> getAllUserInfoPending() throws Exception {
        return ResponseEntity.ok(userManageService.getAllUserNotVerify());
    }
    @RequestMapping(value = "/getInfoUser", method = RequestMethod.POST)
    public ResponseEntity<?> getInfoUser(@RequestBody GetUserInfoRequest getUserInfoRequest) throws Exception {
        return ResponseEntity.ok(userManageService.getInfoUser(getUserInfoRequest));
    }
    @RequestMapping(path = "/changeUserInfo", method = RequestMethod.POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public boolean changeUserInfo(@RequestParam("data") String data,@RequestParam(value = "image",required = false) MultipartFile image) throws Exception {
        return userManageService.ChangeUserInfo(data,image);
    }
    @RequestMapping(value = "/acceptChangeUserInfo", method = RequestMethod.POST)
    public boolean acceptChangeUserInfo(@RequestBody AcceptChangeUserInfo acceptChangeUserInfo) throws Exception {
        return userManageService.AcceptChangeUserInfo(acceptChangeUserInfo);
    }

    @RequestMapping(value = "/getAllUserInfo", method = RequestMethod.GET)
    public ResponseEntity<?> getAllUserInfo() {
        return ResponseEntity.ok(userManageService.getAllUserInfo());
    }
}
