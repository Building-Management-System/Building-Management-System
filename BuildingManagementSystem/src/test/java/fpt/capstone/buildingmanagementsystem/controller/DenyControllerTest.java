package fpt.capstone.buildingmanagementsystem.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DenyControllerTest {
    DenyController denyController = new DenyController();

    @Test
    void testFirstPage() {
        String result = denyController.firstPage();
        Assertions.assertEquals("deny access", result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme