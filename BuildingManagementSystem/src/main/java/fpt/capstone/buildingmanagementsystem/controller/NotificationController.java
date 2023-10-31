package fpt.capstone.buildingmanagementsystem.controller;

import fpt.capstone.buildingmanagementsystem.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@RestController
@CrossOrigin
public class NotificationController {
    @Autowired
    NotificationService notificationService;
    @RequestMapping(path = "/saveNewNotification", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public boolean saveNewNotification(@RequestParam("data") String data, @RequestParam(value = "image", required = false) MultipartFile[] image,
    @RequestParam(value = "file", required = false) MultipartFile[] file) throws Exception {
        return notificationService.saveNotification(data, image,file);
    }
}
