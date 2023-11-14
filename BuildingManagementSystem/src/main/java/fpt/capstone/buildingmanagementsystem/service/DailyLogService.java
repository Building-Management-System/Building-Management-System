package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.model.entity.Account;
import fpt.capstone.buildingmanagementsystem.model.entity.ControlLogLcd;
import fpt.capstone.buildingmanagementsystem.model.entity.DailyLog;
import fpt.capstone.buildingmanagementsystem.model.entity.User;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.DateType;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.LateType;
import fpt.capstone.buildingmanagementsystem.model.response.LateFormResponse;
import fpt.capstone.buildingmanagementsystem.repository.AccountRepository;
import fpt.capstone.buildingmanagementsystem.repository.DailyLogRepository;
import fpt.capstone.buildingmanagementsystem.repository.LateRequestFormRepositoryV2;
import fpt.capstone.buildingmanagementsystem.repository.UserRepository;
import fpt.capstone.buildingmanagementsystem.service.schedule.CheckoutAnalyzeSchedule;
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

import static fpt.capstone.buildingmanagementsystem.until.Until.roundDouble;
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

    @Autowired
    CheckoutAnalyzeSchedule checkoutAnalyzeSchedule;

    private static final Time startMorningTime = Time.valueOf("08:30:00");

    private static final Time endMorningTime = Time.valueOf("12:00:00");

    private static final Time startAfternoonTime = Time.valueOf("13:00:00");

    private static final double One_hour = 1000 * 60 * 60;

    private static final double One_minute = 1000 * 60;

    private static final Logger logger = LoggerFactory.getLogger(DailyLogService.class);


    public DailyLog mapControlLogToDailyLog(ControlLogLcd controlLogLcd) {
        Account account = accountRepository.findByUsername(controlLogLcd.getPersionName())
                .orElseThrow(() -> new BadRequest("Not_found_account"));
        Date dailyDate = new Date(controlLogLcd.getTime().getTime());
        Time dailyTime = new Time(controlLogLcd.getTime().getTime());

        Optional<DailyLog> dailyLogOptional = dailyLogRepository.findByUserAndDate(account.getUser(), dailyDate);

        if (dailyLogOptional.isPresent()) {
            DailyLog dailyLog = updateExistedDailyLog(dailyLogOptional.get(), dailyTime);
            logger.info(dailyLog + "");
            return dailyLogRepository.save(dailyLog);
        } else {
            return addNewDailyLog(dailyDate, dailyTime, account);
        }
    }

    public DailyLog updateExistedDailyLog(DailyLog dailyLog, Time checkoutTime) {

        if (compareTime(checkoutTime, endMorningTime) < 0) {
            dailyLog.setCheckout(checkoutTime);
            dailyLog.setSystemCheckOut(checkoutTime);

            double morningTotal = roundDouble(getDistanceTime(checkoutTime, dailyLog.getCheckin()) / One_hour);
            dailyLog.setMorningTotal(morningTotal);

            dailyLog.setAfternoonTotal(0);

        } else {
            dailyLog.setCheckout(checkoutTime);
            dailyLog.setSystemCheckOut(checkoutTime);

            double morningTotal = roundDouble(getDistanceTime(endMorningTime, dailyLog.getCheckin()) / One_hour);
            dailyLog.setMorningTotal(morningTotal);
        }

        if (compareTime(dailyLog.getCheckin(), startAfternoonTime) < 0) {
            dailyLog.setCheckout(checkoutTime);
            dailyLog.setSystemCheckOut(checkoutTime);

            if (compareTime(checkoutTime, startAfternoonTime) > 0) {
                double afternoonTotal = roundDouble(getDistanceTime(checkoutTime, startAfternoonTime) / One_hour);
                dailyLog.setAfternoonTotal(afternoonTotal);
            }
        } else {
            dailyLog.setCheckout(checkoutTime);
            dailyLog.setSystemCheckOut(checkoutTime);

            double afternoonTotal = roundDouble(getDistanceTime(checkoutTime, dailyLog.getCheckin()) / One_hour);
            dailyLog.setAfternoonTotal(afternoonTotal);
            dailyLog.setMorningTotal(0);
        }

        dailyLog.setTotalAttendance(roundDouble(dailyLog.getMorningTotal() + dailyLog.getAfternoonTotal()));

        if (dailyLog.getDateType().equals(DateType.NORMAL)) {
            dailyLog.setPaidDay(Math.min(roundDouble(dailyLog.getTotalAttendance() / 8), 1));
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
                .systemCheckIn(checkinTime)
                .systemCheckOut(checkinTime)
                .build();
        LocalDate localDate = dailyLog.getDate().toLocalDate();
        dailyLog.setMonth(localDate.getMonthValue());
        dailyLog.setDateType(getDateType(dailyDate));
        getLateCheckInDuration(dailyLog, account.getUser().getUserId(), dailyDate, checkinTime);

        return dailyLogRepository.save(dailyLog);
    }

    public void getLateCheckInDuration(DailyLog dailyLog, String userId, Date date, Time checkinTime) {
        if (!dailyLog.getDateType().equals(DateType.NORMAL)) return;
        if (compareTime(checkinTime, startMorningTime) > 0) {
            List<LateFormResponse> findLateMorningAccepted = lateRequestFormRepositoryV2.findLateAndEarlyViolateByUserIdAndDate(userId, date, LateType.LATE_MORNING);
            dailyLog.setLateCheckin(true);
            if (!findLateMorningAccepted.isEmpty()) {
                List<LateFormResponse> lateAccepted = findLateMorningAccepted.stream().sorted(Comparator.comparing(LateFormResponse::getLateDuration).reversed())
                        .collect(Collectors.toList());
                double lateMinutes = roundDouble((startMorningTime.getTime() - checkinTime.getTime()) / One_minute);
                if (lateMinutes > lateAccepted.get(0).getLateDuration()) {
                    dailyLog.setViolate(true);
                }
            } else {
                dailyLog.setViolate(true);
            }
        } else {
            dailyLog.setLateCheckin(false);
        }
    }

    public static DateType getDateType(Date dailyDate) {
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

    public void updateDailyLog(User user, Date date, Time checkin, Time checkoutTime) {
        DailyLog dailyLog = dailyLogRepository.findByUserAndDate(user, date)
                .orElseThrow(() -> new BadRequest("Not_found_log"));
        dailyLog.setCheckin(checkin);

        if (compareTime(checkoutTime, endMorningTime) < 0) {
            dailyLog.setCheckout(checkoutTime);

            double morningTotal = roundDouble(getDistanceTime(checkoutTime, dailyLog.getCheckin()) / One_hour);
            dailyLog.setMorningTotal(morningTotal);

            dailyLog.setAfternoonTotal(0);

        } else {
            dailyLog.setCheckout(checkoutTime);

            double morningTotal = roundDouble(getDistanceTime(endMorningTime, dailyLog.getCheckin()) / One_hour);
            dailyLog.setMorningTotal(morningTotal);
        }

        if (compareTime(dailyLog.getCheckin(), startAfternoonTime) < 0) {
            dailyLog.setCheckout(checkoutTime);

            if (compareTime(checkoutTime, startAfternoonTime) > 0) {
                double afternoonTotal = roundDouble(getDistanceTime(checkoutTime, startAfternoonTime) / One_hour);
                dailyLog.setAfternoonTotal(afternoonTotal);
            }
        } else {
            dailyLog.setCheckout(checkoutTime);

            double afternoonTotal = roundDouble(getDistanceTime(checkoutTime, dailyLog.getCheckin()) / One_hour);
            dailyLog.setAfternoonTotal(afternoonTotal);
            dailyLog.setMorningTotal(0);
        }

        dailyLog.setTotalAttendance((dailyLog.getMorningTotal() + dailyLog.getAfternoonTotal()));

        if (dailyLog.getDateType().equals(DateType.NORMAL)) {
            dailyLog.setPaidDay(Math.min(roundDouble(dailyLog.getTotalAttendance() / 8), 1));
        }
        getLateCheckInDuration(dailyLog, user.getUserId(), date, dailyLog.getCheckin());
        checkoutAnalyzeSchedule.checkViolate(dailyLog, user.getAccount(), date);
    }

    public void checkViolate(DailyLog dailyLog, User user) {
        getLateCheckInDuration(dailyLog, user.getUserId(), dailyLog.getDate(), dailyLog.getCheckin());
        checkoutAnalyzeSchedule.checkViolate(dailyLog, user.getAccount(), dailyLog.getDate());
    }
}

