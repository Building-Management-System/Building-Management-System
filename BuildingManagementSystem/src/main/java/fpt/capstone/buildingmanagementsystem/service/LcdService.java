package fpt.capstone.buildingmanagementsystem.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.Base64;
import fpt.capstone.buildingmanagementsystem.model.entity.ControlLogLcd;
import fpt.capstone.buildingmanagementsystem.repository.ControlLogLcdRepository;
import fpt.capstone.buildingmanagementsystem.service.schedule.TicketRequestScheduledService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
public class LcdService {

    @Autowired
    ControlLogLcdRepository controlLogLcdRepository;

    private static final Logger logger = LoggerFactory.getLogger(TicketRequestScheduledService.class);
    private static final String CONTROL_LOG = "RecPush";

    private static final String STRANGER_LOG = "StrSnapPush";

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void ExtractJsonLcdLog(String jsonStr) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonStr);
            JsonNode infoNode = rootNode.path("info");

            String operator = rootNode.get("operator").asText();

            if (operator.equals(CONTROL_LOG)) {
                ControlLogLcd controlLogLcd = ControlLogLcd.builder()
                        .operator(rootNode.path("operator").asText())
                        .personId(infoNode.path("personId").asText())
                        .recordId(infoNode.path("RecordID").asInt())
                        .verifyStatus(infoNode.path("VerifyStatus").asInt())
                        .similarity1(infoNode.path("similarity1").asDouble())
                        .similarity2(infoNode.path("similarity2").asDouble())
                        .persionName(infoNode.path("persionName").asText())
                        .telnum(infoNode.path("telnum").asText())
                        .time(formatter.parse(infoNode.path("time").asText()))
                        .pic(convertBase64ToByteArray(infoNode.path("pic").asText()))
                        .build();
                logger.info(controlLogLcd + "");
                controlLogLcdRepository.save(controlLogLcd);
            }

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] convertBase64ToByteArray(String base64Str) {
        return Base64.decodeBase64(base64Str);
    }
}
