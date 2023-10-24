package fpt.capstone.buildingmanagementsystem.controller;

import fpt.capstone.buildingmanagementsystem.model.request.OtherFormRequest;
import fpt.capstone.buildingmanagementsystem.service.RequestOtherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class OtherFormController {
    @Autowired
    private RequestOtherService requestOtherService;

    @GetMapping("closeOtherRequest")
    public boolean acceptOtherRequest(@RequestBody OtherFormRequest otherFormRequest) {
        return requestOtherService.closeOtherRequest(otherFormRequest.getTicketId());
    }
}
