package fpt.capstone.buildingmanagementsystem.until;

import fpt.capstone.buildingmanagementsystem.security.PasswordEncode;
import org.springframework.stereotype.Component;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;
@Component
public class Until {
    public static String generateId() {
        return UUID.randomUUID().toString();
    }
    public static String generateRealTime(){
        ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime nowAtVietNam = now.withZoneSameInstant(zoneId);
        Instant instant= Timestamp.valueOf(nowAtVietNam.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).toInstant();
        return instant.toString();
    }
    public static String encodePassword(String password){
        return new PasswordEncode().passwordEncoder().encode(password);
    }
    public static String getRandomString(int n) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return sb.toString();
    }
}
