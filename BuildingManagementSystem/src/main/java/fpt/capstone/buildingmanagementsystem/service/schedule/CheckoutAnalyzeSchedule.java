package fpt.capstone.buildingmanagementsystem.service.schedule;

import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.exception.NotFound;
import fpt.capstone.buildingmanagementsystem.model.entity.Account;
import fpt.capstone.buildingmanagementsystem.model.entity.DailyLog;
import fpt.capstone.buildingmanagementsystem.model.entity.DayOff;
import fpt.capstone.buildingmanagementsystem.model.entity.User;
import fpt.capstone.buildingmanagementsystem.model.entity.requestForm.LeaveRequestForm;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.DateType;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.LateType;
import fpt.capstone.buildingmanagementsystem.model.response.LateFormResponse;
import fpt.capstone.buildingmanagementsystem.repository.AccountRepository;
import fpt.capstone.buildingmanagementsystem.repository.ControlLogLcdRepository;
import fpt.capstone.buildingmanagementsystem.repository.DailyLogRepository;
import fpt.capstone.buildingmanagementsystem.repository.DayOffRepository;
import fpt.capstone.buildingmanagementsystem.repository.LateRequestFormRepositoryV2;
import fpt.capstone.buildingmanagementsystem.repository.LeaveRequestFormRepository;
import fpt.capstone.buildingmanagementsystem.repository.UserRepository;
import fpt.capstone.buildingmanagementsystem.service.DailyLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static fpt.capstone.buildingmanagementsystem.validate.Validate.compareTime;
import static fpt.capstone.buildingmanagementsystem.validate.Validate.getDistanceTime;

@Component
public class CheckoutAnalyzeSchedule {

    @Autowired
    ControlLogLcdRepository controlLogLcdRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DailyLogRepository dailyLogRepository;

    @Autowired
    DayOffRepository dayOffRepository;

    @Autowired
    LeaveRequestFormRepository leaveRequestFormRepository;

    @Autowired
    LateRequestFormRepositoryV2 lateRequestFormRepository;

    private static final Time endAfternoonTime = Time.valueOf("17:30:00");

    private static final double One_hour = 1000 * 60 * 60;

    private static final Logger logger = LoggerFactory.getLogger(CheckoutAnalyzeSchedule.class);

    //cron = "0 01 0 * * ?"
    @Scheduled(cron = "0 15 00 * * ?")
    @Transactional
    public void scheduledCheckoutAnalyst() {

        List<User>  users = userRepository.findAll();

        List<DailyLog> dailyLogs = dailyLogRepository.findByDate(getYesterdayDate());
        Date yesterday = getYesterdayDate();
        dailyLogs.forEach(dailyLog -> getPersonalLastCheckout(dailyLog.getUser().getAccount(), yesterday));

        Map<String, DailyLog> dailyMap = dailyLogs.stream()
                .collect(Collectors.toMap(dailyLog -> dailyLog.getUser().getUserId(), Function.identity()));

        List<User> userOffs = users.stream()
                .filter(user -> !dailyMap.containsKey(user.getUserId()))
                .collect(Collectors.toList());
        List<DailyLog> workOffs = new ArrayList<>();
        userOffs.forEach(userOff -> {
            DailyLog workOff = getOffWorkDaily(userOff.getAccount(), yesterday);
            workOffs.add(workOff);
        });
        dailyLogRepository.saveAll(workOffs);
    }

    private Date getYesterdayDate() {
        Date today = new Date(System.currentTimeMillis());

        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.DATE, -1);
        return new java.sql.Date(cal.getTimeInMillis());
    }

    private void getPersonalLastCheckout(Account account, Date yesterday) {
        DailyLog dailyLog = dailyLogRepository.getLastCheckoutOfDateByUserId(account.getAccountId(), yesterday)
                .orElseThrow(() -> new BadRequest("Not_found"));

        checkViolate(dailyLog, account, yesterday);
        logger.info(dailyLog + "");
        dailyLogRepository.save(dailyLog);
    }

    private DailyLog getOffWorkDaily(Account account, Date yesterday) {
        DailyLog offWork = DailyLog.builder()
                .date(yesterday)
                .checkin(null)
                .checkout(null)
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
                .systemCheckIn(null)
                .systemCheckOut(null)
                .build();
        LocalDate localDate = yesterday.toLocalDate();
        offWork.setMonth(localDate.getMonthValue());
        offWork.setDateType(DailyLogService.getDateType(yesterday));
        checkLeaveViolate(account, offWork, yesterday);
        return offWork;
    }

    private void checkLeaveViolate(Account account, DailyLog offWork, Date date) {
        if(!offWork.getDateType().equals(DateType.NORMAL)) return;
        List<LeaveRequestForm> leaveRequestForms = leaveRequestFormRepository.findRequestByUserIdAndDate(account.getAccountId(), date);
        if(leaveRequestForms.isEmpty()) {
            offWork.setNonPermittedLeave(8);
            offWork.setViolate(true);
        } else {
            double permittedLeft = getPermittedLeaveLeft(account, offWork.getMonth());
            if(permittedLeft > 8) {
                offWork.setPermittedLeave(8);
                updateDayOffLeft(offWork.getMonth(), account, permittedLeft - 8);
            } else {
                offWork.setPermittedLeave(permittedLeft);
                offWork.setNonPermittedLeave(8-permittedLeft);
                updateDayOffLeft(offWork.getMonth(), account, 0);
            }
        }
    }

    public void checkViolate(DailyLog dailyLog, Account account, Date date) {
        if (dailyLog.isViolate()) return;
        if(!dailyLog.getDateType().equals(DateType.NORMAL)) return;
        List<LateFormResponse> lateFormResponses = lateRequestFormRepository.findLateAndEarlyViolateByUserIdAndDate(account.accountId, date, LateType.EARLY_AFTERNOON)
                .stream().sorted(Comparator.comparing(LateFormResponse::getLateDuration).reversed())
                .collect(Collectors.toList());
        if (compareTime(dailyLog.getCheckout(), endAfternoonTime) < 0) {
            dailyLog.setEarlyCheckout(true);
            if (lateFormResponses.isEmpty()) dailyLog.setViolate(true);
        } else {
            dailyLog.setEarlyCheckout(false);
        }
        List<LeaveRequestForm> leaveRequestForms = leaveRequestFormRepository.findRequestByUserIdAndDate(account.getAccountId(), date);
        if (getDistanceTime(dailyLog.getCheckout(), dailyLog.getCheckin()) / One_hour < 6) {
            double workingHours = getDistanceTime(dailyLog.getCheckout(), dailyLog.getCheckin()) / One_hour;
            double offHours = 8 - workingHours;
            if (leaveRequestForms.isEmpty()) {
                dailyLog.setNonPermittedLeave(offHours);
                dailyLog.setViolate(true);
            } else {
                double permittedLeaveLeft = getPermittedLeaveLeft(account, dailyLog.getMonth());
                if (offHours <= permittedLeaveLeft) {
                    dailyLog.setPermittedLeave(offHours);
                    updateDayOffLeft(dailyLog.getMonth(), account, permittedLeaveLeft - offHours);
                } else {
                    dailyLog.setPermittedLeave(permittedLeaveLeft);
                    dailyLog.setNonPermittedLeave(offHours - permittedLeaveLeft);
                    updateDayOffLeft(dailyLog.getMonth(), account, 0);
                }
            }
        }

    }

    private double getPermittedLeaveLeft(Account account, int month) {
        double permittedHoursLeft = getDayOffOfMonth(month, account);
        List<DailyLog> dailyLogs = dailyLogRepository.findAllByUser(account.getUser());
        double permittedHours = dailyLogs.stream().mapToDouble(DailyLog::getPermittedLeave)
                .sum();
        return permittedHoursLeft - permittedHours;
    }

    private void updateDayOffLeft(int month, Account account, double hourLeft) {
        DayOff dayOff = dayOffRepository.findByAccount(account)
                .orElseThrow(() -> new BadRequest("Not_found"));

        switch (month) {
            case 1:
                dayOff.setJanuary(hourLeft);
                dayOffRepository.save(dayOff);
                return;
            case 2:
                dayOff.setFebruary(hourLeft);
                dayOffRepository.save(dayOff);
                return;
            case 3:
                dayOff.setApril(hourLeft);
                dayOffRepository.save(dayOff);
                return;
            case 4:
                dayOff.setMarch(hourLeft);
                dayOffRepository.save(dayOff);
                return;
            case 5:
                dayOff.setMay(hourLeft);
                dayOffRepository.save(dayOff);
                return;
            case 6:
                dayOff.setJune(hourLeft);
                dayOffRepository.save(dayOff);
                return;
            case 7:
                dayOff.setJuly(hourLeft);
                dayOffRepository.save(dayOff);
                return;
            case 8:
                dayOff.setAugust(hourLeft);
                dayOffRepository.save(dayOff);
                return;
            case 9:
                dayOff.setSeptember(hourLeft);
                dayOffRepository.save(dayOff);
                return;
            case 10:
                dayOff.setOctober(hourLeft);
                dayOffRepository.save(dayOff);
                return;
            case 11:
                dayOff.setNovember(hourLeft);
                dayOffRepository.save(dayOff);
                return;
            case 12:
                dayOff.setDecember(hourLeft);
                dayOffRepository.save(dayOff);
                return;
            default:
                throw new NotFound("Not_found_month");
        }
    }

    private double getDayOffOfMonth(int month, Account account) {
        DayOff dayOff = dayOffRepository.findByAccount(account)
                .orElseThrow(() -> new BadRequest("Not_found"));

        switch (month) {
            case 1:
                return dayOff.getJanuary();
            case 2:
                return dayOff.getFebruary();
            case 3:
                return dayOff.getApril();
            case 4:
                return dayOff.getMarch();
            case 5:
                return dayOff.getMay();
            case 6:
                return dayOff.getJune();
            case 7:
                return dayOff.getJuly();
            case 8:
                return dayOff.getAugust();
            case 9:
                return dayOff.getSeptember();
            case 10:
                return dayOff.getOctober();
            case 11:
                return dayOff.getNovember();
            case 12:
                return dayOff.getDecember();
            default:
                throw new NotFound("Not_found_month");
        }
    }
}
