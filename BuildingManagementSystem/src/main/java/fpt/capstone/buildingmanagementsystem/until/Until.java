package fpt.capstone.buildingmanagementsystem.until;

import fpt.capstone.buildingmanagementsystem.security.PasswordEncode;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

@Component
public class Until {
    public static String generateId() {
        return UUID.randomUUID().toString();
    }

    public static Date generateRealTime() {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime localDate = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
            String dateString = dtf.format(localDate);
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    public static java.sql.Date convertStringToDate(String date) throws ParseException {
        return new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(date).getTime());
    }
    public static java.sql.Time convertStringToTime(String date) throws ParseException {
        return new java.sql.Time(new SimpleDateFormat("HH:mm:ss").parse(date).getTime());
    }

    public static String encodePassword(String password) {
        return new PasswordEncode().passwordEncoder().encode(password);
    }

    public static String getRandomString(int n) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return sb.toString();
    }
}
