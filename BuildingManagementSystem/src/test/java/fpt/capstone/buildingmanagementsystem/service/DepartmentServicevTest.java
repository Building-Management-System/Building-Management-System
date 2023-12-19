package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.mapper.DepartmentMapper;
import fpt.capstone.buildingmanagementsystem.model.entity.Department;
import fpt.capstone.buildingmanagementsystem.model.response.DepartmentResponse;
import fpt.capstone.buildingmanagementsystem.repository.DepartmentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.*;

class DepartmentServicevTest {
    @Mock
    DepartmentRepository departmentRepository;
    @Mock
    DepartmentMapper departmentMapper;
    @InjectMocks
    DepartmentService departmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllDepartment() {
        when(departmentRepository.findAll()).thenReturn(List.of(new Department("departmentId", "departmentName")));
        when(departmentMapper.convert(any())).thenReturn(List.of(new DepartmentResponse("departmentId", "departmentName")));

        List<DepartmentResponse> result = departmentService.getAllDepartment();
        Assertions.assertEquals(List.of(new DepartmentResponse("departmentId", "departmentName")), result);
    }

    @Test
    void testGetDepartmentByManagerRole() {
        when(departmentRepository.getDepartmentWithManagerRole()).thenReturn(List.of(new Department("departmentId", "departmentName")));

        List<DepartmentResponse> result = departmentService.getDepartmentByManagerRole();
        Assertions.assertEquals(List.of(new DepartmentResponse("departmentId", "departmentName")), result);
    }

    @Test
    void testGetDepartmentHaveManager() {
        when(departmentRepository.findAll()).thenReturn(List.of(new Department("departmentId", "departmentName")));

        List<DepartmentResponse> result = departmentService.getDepartmentHaveManager();
        Assertions.assertEquals(List.of(new DepartmentResponse("departmentId", "departmentName")), result);
    }
}
