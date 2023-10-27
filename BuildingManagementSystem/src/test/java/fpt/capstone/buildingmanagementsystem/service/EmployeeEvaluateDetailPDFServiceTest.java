package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.model.entity.Department;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.EvaluateEnum;
import fpt.capstone.buildingmanagementsystem.model.request.MonthlyEvaluateRequest;
import fpt.capstone.buildingmanagementsystem.model.response.FilePdfResponse;
import fpt.capstone.buildingmanagementsystem.model.response.MonthlyEvaluateResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.mockito.Mockito.*;

class EmployeeEvaluateDetailPDFServiceTest {
    @Mock
    MonthlyEvaluateService monthlyEvaluateService;
    @InjectMocks
    EmployeeEvaluateDetailPDFService employeeEvaluateDetailPDFService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExport() throws IOException {
        when(monthlyEvaluateService.getMonthlyEvaluateOfEmployee(any())).thenReturn(new MonthlyEvaluateResponse("evaluateId", 0d, 0d, 0, 0, 0d, 0d, 0, 0d, 0d, 0, 0, EvaluateEnum.GOOD, "note", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 2).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 2).getTime(), "createdBy", "UsernameCreatedBy", "employeeId", "employeeUserName", "firstNameEmp", "lastNameEmp", new Department("departmentId", "departmentName"), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 2).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 2).getTime(), true, "acceptedHrId", "acceptedHrUserName", "hrNote"));

        FilePdfResponse result = employeeEvaluateDetailPDFService.export(new MonthlyEvaluateRequest("userId", 0, 0));
        Assertions.assertEquals(new FilePdfResponse("fileName", "fileContentType", "file"), result);
    }
    //3
}
