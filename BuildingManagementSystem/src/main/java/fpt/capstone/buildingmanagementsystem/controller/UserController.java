package fpt.capstone.buildingmanagementsystem.controller;

import fpt.capstone.buildingmanagementsystem.service.UserManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
public class UserController {
    @Autowired
    UserManageService userManageService;
    @RequestMapping(path = "/changeUserInfo", method = RequestMethod.POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public boolean changeUserInfo(@RequestParam("data") String data,@RequestParam("image") MultipartFile image) throws Exception {
        return userManageService.ChangeUserInfo(data,image);
    }
}
