package fpt.capstone.buildingmanagementsystem.controller;

import fpt.capstone.buildingmanagementsystem.model.response.NotificationDetailResponse;
import fpt.capstone.buildingmanagementsystem.model.response.NotificationTitleResponse;
import fpt.capstone.buildingmanagementsystem.service.NotificationService;
import fpt.capstone.buildingmanagementsystem.service.NotificationServiceV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin
public class NotificationController {
    @Autowired
    NotificationService notificationService;

    @Autowired
    NotificationServiceV2 notificationServiceV2;

    @RequestMapping(path = "/saveNewNotification", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public boolean saveNewNotification(@RequestParam("data") String data, @RequestParam(value = "image", required = false) MultipartFile[] image,
                                       @RequestParam(value = "file", required = false) MultipartFile[] file) throws Exception {
        return notificationService.saveNotification(data, image, file);
    }

    @GetMapping("/getNotificationByUserId")
    public NotificationTitleResponse getAllNotificationByUser(@RequestParam("userId") String userId) {
        return notificationServiceV2.getAllNotificationByUser(userId);
    }

    @GetMapping("/getListNotificationByUserId")
    public List<NotificationDetailResponse> getListNotificationByUserId(@RequestParam("userId") String userId) {
        return notificationServiceV2.getListNotificationByUserId(userId);
    }
}
