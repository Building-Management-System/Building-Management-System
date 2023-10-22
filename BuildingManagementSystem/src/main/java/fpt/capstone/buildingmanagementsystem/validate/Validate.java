package fpt.capstone.buildingmanagementsystem.validate;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

@Component
public class Validate {
    public static final String DATE_FORMAT="^\\d{4}\\-(0[1-9]|1[0-2])\\-(0[1-9]|1\\d|2\\d|3[01])$";
    public static final String TIME_FORMAT="^(2[0-3]|1[0-9]||0[0-9])\\:([0-5][0-9]|)\\:([0-5][0-9]|)$";
    public static boolean validateDateFormat(String date){
        if(Pattern.matches(DATE_FORMAT,date)){
            return true;
        }
        return false;
    }
    public static boolean validateDateTime(String time){
        if(Pattern.matches(TIME_FORMAT,time)){
            return true;
        }
        return false;
    }
    public static boolean validateStartTimeAndEndTime(String startTime,String endTime) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date startTime1 = dateFormat.parse(startTime);
        Date endTime1 = dateFormat.parse(endTime);
        Timestamp timestampStartTime = new java.sql.Timestamp(startTime1.getTime());
        Timestamp timestampEndTime = new java.sql.Timestamp(endTime1.getTime());
        if(timestampEndTime.getTime()-timestampStartTime.getTime()<=0){
            return false;
        }
        return true;
    }
    public static boolean validateStartDateAndEndDate(String startDate,String endDate) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate1 = dateFormat.parse(startDate);
        Date endDate1 = dateFormat.parse(endDate);
        Timestamp timestampStartTime = new java.sql.Timestamp(startDate1.getTime());
        Timestamp timestampEndTime = new java.sql.Timestamp(endDate1.getTime());
        if(timestampEndTime.getTime()-timestampStartTime.getTime()<=0){
            return false;
        }
        return true;
    }
}
