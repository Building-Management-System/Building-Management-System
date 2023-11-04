package fpt.capstone.buildingmanagementsystem.validate;

import fpt.capstone.buildingmanagementsystem.until.Until;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

@Component
public class Validate {
    public static final String DATE_FORMAT="^\\d{4}\\-(0[1-9]|1[0-2])\\-(0[1-9]|1\\d|2\\d|3[01])$";
    public static final String TIME_FORMAT="^(2[0-3]|1[0-9]||0[0-9])\\:([0-5][0-9]|)\\:([0-5][0-9]|)$";
    public static final String DATE_TIME_FORMAT="^\\d{4}\\-(0[1-9]|1[0-2])\\-(0[1-9]|1\\d|2\\d|3[01])\\s(2[0-3]|1[0-9]||0[0-9])\\:([0-5][0-9]|)\\:([0-5][0-9]|)$";
    public static boolean validateDateFormat(String date){
        if(Pattern.matches(DATE_FORMAT,date.toString())){
            return true;
        }
        return false;
    }
    public static boolean validateDateTime(String time){
        if(Pattern.matches(TIME_FORMAT,time.toString())){
            return true;
        }
        return false;
    }
    public static boolean validateDateAndTime(String datetime){
        if(Pattern.matches(DATE_TIME_FORMAT,datetime.toString())){
            return true;
        }
        return false;
    }
    public static boolean validateStartTimeAndEndTime(String startTime,String endTime) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date startTime1 = dateFormat.parse(startTime.toString());
        Date endTime1 = dateFormat.parse(endTime.toString());
        Timestamp timestampStartTime = new java.sql.Timestamp(startTime1.getTime());
        Timestamp timestampEndTime = new java.sql.Timestamp(endTime1.getTime());
        if(timestampEndTime.getTime()-timestampStartTime.getTime()<=0){
            return false;
        }
        return true;
    }

    public static boolean validateStartDateAndEndDate(String startDate, String endDate) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate1 = dateFormat.parse(startDate.toString());
        Date endDate1 = dateFormat.parse(endDate.toString());
        Timestamp timestampStartTime = new java.sql.Timestamp(startDate1.getTime());
        Timestamp timestampEndTime = new java.sql.Timestamp(endDate1.getTime());
        if(timestampEndTime.getTime()-timestampStartTime.getTime()<0){
            return false;
        }
        return true;
    }
    public static boolean checkDateLeave(String startDate) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date realDate = Until.generateDate();
        Date startDate1 = dateFormat.parse(startDate.toString());
        Timestamp timestampStartTime = new java.sql.Timestamp(startDate1.getTime());
        Timestamp timestampRealDate = new java.sql.Timestamp(realDate.getTime());
        if(timestampStartTime.getTime()-timestampRealDate.getTime()<0){
            return false;
        }
        return true;
    }
    public static boolean checkUploadDateRealTime(String startDate) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 4);
        Date realDate = calendar.getTime();
        Date startDate1 = dateFormat.parse(startDate.toString());
        Timestamp timestampStartTime = new java.sql.Timestamp(startDate1.getTime());
        Timestamp timestampRealDate = new java.sql.Timestamp(realDate.getTime());
        if(timestampStartTime.getTime()-timestampRealDate.getTime()<0){
            return false;
        }
        return true;
    }
    public static boolean checkDateBookingRoom(String startDate,String startTime) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date realDate = Until.generateDate();
        Date startDate1 = dateFormat.parse(startDate.toString());
        Date realtime =Until.generateTime();
        Date startTime1 = timeFormat.parse(startTime.toString());
        Timestamp timestampStartDate = new java.sql.Timestamp(startDate1.getTime());
        Timestamp timestampRealDate = new java.sql.Timestamp(realDate.getTime());
        Timestamp timestampStartTime = new java.sql.Timestamp(startTime1.getTime());
        Timestamp timestampRealTime = new java.sql.Timestamp(realtime.getTime());
        if(timestampStartDate.getTime()-timestampRealDate.getTime()<0){
            return false;
        }
        if(timestampStartDate.getTime()-timestampRealDate.getTime()==0){
            if(timestampStartTime.getTime()-timestampRealTime.getTime()<0){
                return false;
            }
            return true;
        }
        return true;
    }
}
