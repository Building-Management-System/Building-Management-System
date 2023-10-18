package fpt.capstone.buildingmanagementsystem.controller;

import fpt.capstone.buildingmanagementsystem.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DepartmentController {
    @Autowired
    DepartmentService departmentService;
    @RequestMapping(value = "/getAllDepartment", method = RequestMethod.GET)
    public ResponseEntity<?> getAllDepartment() {
        return ResponseEntity.ok(departmentService.getAllDepartment());
    }
}
