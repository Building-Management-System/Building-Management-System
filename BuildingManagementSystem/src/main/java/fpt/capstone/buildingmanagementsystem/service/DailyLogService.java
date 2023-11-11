package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.model.entity.Account;
import fpt.capstone.buildingmanagementsystem.model.entity.ControlLogLcd;
import fpt.capstone.buildingmanagementsystem.model.entity.DailyLog;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.DateType;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.LateType;
import fpt.capstone.buildingmanagementsystem.model.response.LateFormResponse;
import fpt.capstone.buildingmanagementsystem.repository.AccountRepository;
import fpt.capstone.buildingmanagementsystem.repository.DailyLogRepository;
import fpt.capstone.buildingmanagementsystem.repository.LateRequestFormRepositoryV2;
import fpt.capstone.buildingmanagementsystem.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static fpt.capstone.buildingmanagementsystem.validate.Validate.compareTime;
import static fpt.capstone.buildingmanagementsystem.validate.Validate.getDistanceTime;

@Service
public class DailyLogService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DailyLogRepository dailyLogRepository;

    @Autowired
    LateRequestFormRepositoryV2 lateRequestFormRepositoryV2;

    private static final Time startMorningTime = Time.valueOf("08:30:00");

    private static final Time endMorningTime = Time.valueOf("12:00:00");

    private static final Time startAfternoonTime = Time.valueOf("13:00:00");

    private static final double One_hour = 1000 * 60 * 60;

    private static final double One_minute = 1000 * 60;


    private static final Time endAfternoonTime = Time.valueOf("17:30:00");

    private static final Logger logger = LoggerFactory.getLogger(DailyLogService.class);


    public DailyLog mapControlLogToDailyLog(ControlLogLcd controlLogLcd) {
        Account account = accountRepository.findByUsername(controlLogLcd.getPersionName())
                .orElseThrow(() -> new BadRequest("Not_found_account"));

        Date dailyDate = new Date(controlLogLcd.getTime().getTime());
        Time dailyTime = new Time(controlLogLcd.getTime().getTime());

        Optional<DailyLog> dailyLogOptional = dailyLogRepository.findByUserAndDate(account.getUser(), dailyDate);

        if (dailyLogOptional.isPresent()) {
            DailyLog dailyLog = updateDailyLogWhenExisted(dailyLogOptional.get(), dailyTime);
            logger.info(dailyLog + "");
            return dailyLogRepository.save(dailyLog);
        } else {
            return addNewDailyLog(dailyDate, dailyTime, account);
        }
    }

    private DailyLog updateDailyLogWhenExisted(DailyLog dailyLog, Time checkoutTime) {

        if (compareTime(checkoutTime, endMorningTime) < 0) {
            dailyLog.setCheckout(checkoutTime);

            double morningTotal = getDistanceTime(checkoutTime, dailyLog.getCheckin()) / One_hour;
            dailyLog.setMorningTotal(morningTotal);

        } else {
            dailyLog.setCheckout(checkoutTime);

            double morningTotal = getDistanceTime(endMorningTime, dailyLog.getCheckin()) / One_hour;
            dailyLog.setMorningTotal(morningTotal);
        }

        if (compareTime(dailyLog.getCheckin(), startAfternoonTime) < 0) {
            dailyLog.setCheckout(checkoutTime);
            if (compareTime(checkoutTime, startAfternoonTime) > 0) {
                double afternoonTotal = getDistanceTime(checkoutTime, startAfternoonTime) / One_hour;
                dailyLog.setAfternoonTotal(afternoonTotal);
            }
        } else {
            dailyLog.setCheckout(checkoutTime);
            double afternoonTotal = getDistanceTime(checkoutTime, dailyLog.getCheckin()) / One_hour;
            dailyLog.setAfternoonTotal(afternoonTotal);
        }

        dailyLog.setTotalAttendance(getDistanceTime(checkoutTime, dailyLog.getCheckin()) / One_hour);

        if (dailyLog.getDateType().equals(DateType.NORMAL)) {
            dailyLog.setPaidDay(Math.min(dailyLog.getTotalAttendance() / 8, 1));
        }

        return dailyLog;
    }

    private DailyLog addNewDailyLog(Date dailyDate, Time checkinTime, Account account) {
        DailyLog dailyLog = DailyLog.builder()
                .date(dailyDate)
                .checkin(checkinTime)
                .checkout(checkinTime)
                .totalAttendance(0)
                .morningTotal(0)
                .afternoonTotal(0)
                .earlyCheckout(false)
                .lateCheckin(false)
                .permittedLeave(0)
                .nonPermittedLeave(0)
                .Violate(false)
                .paidDay(0)
                .outsideWork(0)
                .user(account.getUser())
                .build();
        LocalDate localDate = dailyLog.getDate().toLocalDate();
        dailyLog.setMonth(localDate.getMonthValue());
        dailyLog.setDateType(getDateType(dailyDate));
        getLateCheckInDuration(dailyLog, account.getUser().getUserId(), dailyDate, checkinTime);

        return dailyLogRepository.save(dailyLog);
    }

    private void getLateCheckInDuration(DailyLog dailyLog, String userId, Date date, Time checkinTime) {
        if (dailyLog.getDateType().equals(DateType.WEEKEND) || dailyLog.getDateType().equals(DateType.HOLIDAY)) return;
        if (compareTime(checkinTime, startMorningTime) > 0) {
            List<LateFormResponse> findLateMorningAccepted = lateRequestFormRepositoryV2.findLateAndEarlyViolateByUserIdAndDate(userId, date, LateType.LATE_MORNING)
                    .stream().sorted(Comparator.comparing(LateFormResponse::getLateDuration).reversed())
                    .collect(Collectors.toList());
            dailyLog.setLateCheckin(true);
            if (!findLateMorningAccepted.isEmpty()) {
                double lateMinutes = (startMorningTime.getTime() - checkinTime.getTime()) / One_minute;
                if (lateMinutes > findLateMorningAccepted.get(0).getLateDuration()) {
                    dailyLog.setViolate(true);
                }
            } else {
                dailyLog.setViolate(true);
            }
        }
    }

    private DateType getDateType(Date dailyDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dailyDate);

        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        boolean isWeekend = (dayOfWeek == Calendar.SATURDAY) || (dayOfWeek == Calendar.SUNDAY);

        if (isWeekend) {
            return DateType.WEEKEND;
        } else {
            return DateType.NORMAL;
        }
    }


}

