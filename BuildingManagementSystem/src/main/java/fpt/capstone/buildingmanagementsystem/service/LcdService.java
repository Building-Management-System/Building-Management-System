package fpt.capstone.buildingmanagementsystem.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.Base64;
import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.model.entity.Account;
import fpt.capstone.buildingmanagementsystem.model.entity.ControlLogLcd;
import fpt.capstone.buildingmanagementsystem.model.entity.DailyLog;
import fpt.capstone.buildingmanagementsystem.repository.AccountRepository;
import fpt.capstone.buildingmanagementsystem.repository.ControlLogLcdRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
public class LcdService {
    private static final Logger logger = LoggerFactory.getLogger(LcdService.class);
    private static final String CONTROL_LOG = "RecPush";

    private static final String STRANGER_LOG = "StrSnapPush";

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    ControlLogLcdRepository controlLogLcdRepository;

    @Autowired
    DailyLogService dailyLogService;

    @Autowired
    AccountRepository accountRepository;

    public DailyLog ExtractJsonLcdLog(String jsonStr) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonStr);
            JsonNode infoNode = rootNode.path("info");

            String operator = rootNode.get("operator").asText();

            if (operator.equals(CONTROL_LOG)) {
                String time = infoNode.path("time").asText();
                ControlLogLcd controlLogLcd = ControlLogLcd.builder()
                        .operator(rootNode.path("operator").asText())
                        .personId(infoNode.path("personId").asText())
                        .recordId(infoNode.path("RecordID").asInt())
                        .verifyStatus(infoNode.path("VerifyStatus").asInt())
                        .similarity1(infoNode.path("similarity1").asDouble())
                        .similarity2(infoNode.path("similarity2").asDouble())
                        .persionName(infoNode.path("persionName").asText())
                        .telnum(infoNode.path("telnum").asText())
                        .time(formatter.parse(time))
                        .pic(convertBase64ToByteArray(infoNode.path("pic").asText()))
                        .build();
                Account account = accountRepository.findByUsername(controlLogLcd.getPersionName())
                        .orElseThrow(() -> new BadRequest("Not_found"));
                controlLogLcd.setAccount(account);
                controlLogLcdRepository.save(controlLogLcd);
                return dailyLogService.mapControlLogToDailyLog(controlLogLcd);
            } else {

            }
            return null;
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] convertBase64ToByteArray(String base64Str) {
        String image = base64Str.substring("data:image/jpeg;base64,".length());
        return Base64.decodeBase64(image);
    }
}
