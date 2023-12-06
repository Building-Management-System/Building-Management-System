package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.exception.ServerError;
import fpt.capstone.buildingmanagementsystem.model.entity.DailyLog;
import fpt.capstone.buildingmanagementsystem.model.entity.Holiday;
import fpt.capstone.buildingmanagementsystem.model.entity.User;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.DateType;
import fpt.capstone.buildingmanagementsystem.model.request.HolidayDeleteRequest;
import fpt.capstone.buildingmanagementsystem.model.request.HolidaySaveRequest;
import fpt.capstone.buildingmanagementsystem.repository.DailyLogRepository;
import fpt.capstone.buildingmanagementsystem.repository.HolidayRepository;
import fpt.capstone.buildingmanagementsystem.repository.UserRepository;
import fpt.capstone.buildingmanagementsystem.until.Until;
import fpt.capstone.buildingmanagementsystem.validate.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


@Service
public class HolidayService {
    @Autowired
    AttendanceService attendanceService;
    @Autowired
    HolidayRepository holidayRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DailyLogRepository dailyLogRepository;
    //check tất cả daily log nếu có rồi thì biến đổi theo fromDate và toDate
    public boolean saveHoliday(HolidaySaveRequest holidaySaveRequest) throws ParseException {
        if (holidaySaveRequest.getUserId() != null && holidaySaveRequest.getTitle() != null && holidaySaveRequest.getContent() != null
                && holidaySaveRequest.getToDate() != null && holidaySaveRequest.getFromDate() != null) {
            if (Validate.validateStartDateAndEndDate(holidaySaveRequest.getFromDate(), holidaySaveRequest.getToDate())) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date fromDate = dateFormat.parse(holidaySaveRequest.getFromDate());
                    Date sqlfromDate = new Date(fromDate.getTime());
                    java.util.Date toDate = dateFormat.parse(holidaySaveRequest.getToDate());
                    Date sqltoDate = new Date(toDate.getTime());
                    User user = userRepository.findByUserId(holidaySaveRequest.getUserId()).get();
                    Holiday holiday = Holiday.builder().content(holidaySaveRequest.getContent()).title(holidaySaveRequest.getTitle()).createDate(Until.generateRealTime())
                            .updateDate(Until.generateRealTime()).user(user).toDate(sqltoDate).fromDate(sqlfromDate)
                            .build();
                    holidayRepository.save(holiday);
                    List<DailyLog> dailyLogs= dailyLogRepository.getDailyLogsByFromDateAndToDate(sqlfromDate,sqltoDate);
                    List<DailyLog> convertHoliday= new ArrayList<>();
                    dailyLogs.forEach(dailyLog -> {
                        dailyLog.setDateType(DateType.HOLIDAY);
                        convertHoliday.add(dailyLog);
                    });
                    dailyLogRepository.saveAll(convertHoliday);
                    return true;
                } catch (ServerError e) {
                    throw new ServerError("Fail");
                }
            } else {
                throw new BadRequest("from_date_needs_be_greater_than_to_day");
            }
        } else {
            throw new BadRequest("request_fails");
        }
    }

    public boolean deleteHoliday(HolidayDeleteRequest holidayDeleteRequest) {
        if (holidayDeleteRequest.getHolidayId() != null) {
            try {
                Holiday holiday=holidayRepository.findById(holidayDeleteRequest.getHolidayId()).get();
                holidayRepository.deleteById(holidayDeleteRequest.getHolidayId());
                List<DailyLog> dailyLogs= dailyLogRepository.getDailyLogsByFromDateAndToDate(holiday.getFromDate(),holiday.getToDate());
                List<DailyLog> convertHoliday= new ArrayList<>();
                dailyLogs.forEach(dailyLog -> {
                    if ((attendanceService.getCheckWeekend(dailyLog.getDate()) != Calendar.SATURDAY) && (attendanceService.getCheckWeekend(dailyLog.getDate()) != Calendar.SUNDAY)) {
                        dailyLog.setDateType(DateType.NORMAL);
                    }else {
                        dailyLog.setDateType(DateType.WEEKEND);
                    }
                    convertHoliday.add(dailyLog);
                });
                dailyLogRepository.saveAll(convertHoliday);
                return true;
            } catch (Exception e) {
                throw new ServerError("Fail");
            }
        } else {
            throw new BadRequest("request_fails");
        }
    }
    // hàm này sẽ quét nếu ngày hôm trước là holiday thì convert tất sang holiday
    public void changeDailyLogToHoliday(){
        java.util.Date currentDate = Until.generateDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        // Subtract one day
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        // Get the updated date
        java.util.Date yesterday = calendar.getTime();
        Date sqlyesterdayDate = new Date(yesterday.getTime());
        List<Holiday> holidayList=holidayRepository.findAll();
        holidayList.forEach(holiday -> {
            Timestamp timeStampYesterdayDate = new java.sql.Timestamp(sqlyesterdayDate.getTime());
            Timestamp timeStampFromDate = new java.sql.Timestamp(holiday.getFromDate().getTime());
            Timestamp timeStampToDate= new java.sql.Timestamp(holiday.getToDate().getTime());
            if (timeStampToDate.getTime() - timeStampYesterdayDate.getTime() >= 0 &&
                    timeStampYesterdayDate.getTime() - timeStampFromDate.getTime() >= 0) {
                List<DailyLog> dailyLogs= new ArrayList<>();
                dailyLogRepository.findByDate(sqlyesterdayDate).forEach(element -> {
                    element.setDateType(DateType.HOLIDAY);
                    element.setPaidDay(1);
                    element.setLateCheckin(false);
                    element.setEarlyCheckout(false);
                    dailyLogs.add(element);

                });
                dailyLogRepository.saveAll(dailyLogs);
            }
        });
    }
}
