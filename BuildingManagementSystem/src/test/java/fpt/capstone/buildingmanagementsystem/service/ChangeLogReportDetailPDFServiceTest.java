package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.model.response.ChangeLogDetailResponse;
import fpt.capstone.buildingmanagementsystem.model.response.FilePdfResponse;
import fpt.capstone.buildingmanagementsystem.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;

class ChangeLogReportDetailPDFServiceTest {
    @Mock
    AccountRepository accountRepository;
    @Mock
    RequestChangeLogService requestChangeLogService;
    @InjectMocks
    ChangeLogReportDetailPDFService changeLogReportDetailPDFService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExport() throws IOException {
        when(requestChangeLogService.getChangeLogDetail(anyString(), anyString(), anyString())).thenReturn(new ChangeLogDetailResponse("name", "username", "departmentName", "dateDaily", null, null, null, null, "dateDailyChange", "changeFrom", "reason", true, 0d, List.of(null)));

        FilePdfResponse result = changeLogReportDetailPDFService.export("changeLogId", "userId", "date");
        Assertions.assertEquals(new FilePdfResponse("fileName", "fileContentType", "file"), result);
    }
}
