package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.SimpleDateFormat;
import java.util.List;

import static org.mockito.Mockito.*;

class LcdServiceTest {
    @Mock
    SimpleDateFormat formatter;
    @Mock
    ControlLogLcdRepository controlLogLcdRepository;
    @Mock
    DailyLogService dailyLogService;
    @Mock
    AccountRepository accountRepository;
    @Mock
    DeviceRepository deviceRepository;
    @Mock
    StrangerLogLcdRepository strangerLogLcdRepository;
    @Mock
    RoomRepository roomRepository;
    @InjectMocks
    LcdService lcdService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExtractJsonLcdLog() {
        when(accountRepository.findByUsername(anyString())).thenReturn(null);
        when(deviceRepository.findByDeviceIdAndStatus(anyString(), any())).thenReturn(null);
        when(roomRepository.getRoomByDevice(anyString())).thenReturn(List.of(null));

        lcdService.ExtractJsonLcdLog("jsonStr");
    }
}
