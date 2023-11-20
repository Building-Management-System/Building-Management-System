package fpt.capstone.buildingmanagementsystem.controller;

import fpt.capstone.buildingmanagementsystem.model.request.OtherFormRequest;
import fpt.capstone.buildingmanagementsystem.service.RequestOtherService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class OtherFormControllerTest {
    @Autowired
    RequestOtherService requestOtherService;
    @Autowired
    OtherFormController otherFormController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAcceptOtherRequest() {
        OtherFormRequest otherFormRequest = new OtherFormRequest();
        otherFormRequest.setTicketId("OR_7ffe1a71-ca8c-4ca9-ae08-e0e6c443bcee");

        boolean result = otherFormController.acceptOtherRequest(otherFormRequest);
        Assertions.assertEquals(true, result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme