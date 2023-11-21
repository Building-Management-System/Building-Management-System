package fpt.capstone.buildingmanagementsystem.controller;

import fpt.capstone.buildingmanagementsystem.model.request.LateMessageRequest;
import fpt.capstone.buildingmanagementsystem.service.LateRequestService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class LateRequestControllerTest {
    @Autowired
    LateRequestService lateRequestService;
    @Autowired
    LateRequestController lateRequestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAcceptLateRequest() {
        when(lateRequestService.acceptLateRequest(anyString())).thenReturn(true);

        boolean result = lateRequestController.acceptLateRequest(new LateMessageRequest("lateMessageRequestId", "content"));
        Assertions.assertEquals(true, result);
    }

    @Test
    void testRejectLateRequest() {
        when(lateRequestService.rejectLateRequest(any())).thenReturn(true);

        boolean result = lateRequestController.rejectLateRequest(new LateMessageRequest("lateMessageRequestId", "content"));
        Assertions.assertEquals(true, result);
    }
}
