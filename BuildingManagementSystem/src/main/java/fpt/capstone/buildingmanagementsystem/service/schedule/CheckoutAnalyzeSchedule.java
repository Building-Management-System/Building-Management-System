package fpt.capstone.buildingmanagementsystem.service.schedule;

import fpt.capstone.buildingmanagementsystem.model.entity.Account;
import fpt.capstone.buildingmanagementsystem.repository.AccountRepository;
import fpt.capstone.buildingmanagementsystem.repository.ControlLogLcdRepository;
import fpt.capstone.buildingmanagementsystem.repository.DailyLogRepository;
import fpt.capstone.buildingmanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.Calendar;

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

    @Scheduled(cron = "")
    public void scheduledCheckoutAnalyst() {

        accountRepository.findAll()
                .forEach(account -> {

                });
    }

    private Date getYesterdayDate() {
        Date today = new Date(System.currentTimeMillis());

        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.DATE, -1);

        return new java.sql.Date(cal.getTimeInMillis());
    }

    private void getPersonalLastCheckout(Account account, Date yesterday) {

    }
}
