package fpt.capstone.buildingmanagementsystem.service.schedule;

import fpt.capstone.buildingmanagementsystem.exception.ServerError;
import fpt.capstone.buildingmanagementsystem.model.entity.EmailCode;
import fpt.capstone.buildingmanagementsystem.repository.EmailCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class VerifyCodeScheduleDeleteService {
    @Autowired
    EmailCodeRepository emailCodeRepository;
    @Scheduled(cron = "0 0/2 * * * ?")
    public void getAllNotificationScheduled(){
        List<EmailCode> emailCodeList= emailCodeRepository.findAllByInTaskDelete(false);
        if(emailCodeList.size()>0) {
            for (EmailCode emailCode : emailCodeList) {
                Timer timer = new Timer();
                Date scheduledTime = new Date();
                scheduledTime.setTime(emailCode.getDeleteTime().getTime());
                DeleteEmailCode delete = new DeleteEmailCode(emailCode, emailCodeRepository);
                timer.schedule(delete, scheduledTime);
                emailCode.setInTaskDelete(true);
                emailCodeRepository.save(emailCode);
            }
        }
    }
}
class DeleteEmailCode extends TimerTask {
    private final EmailCode emailCode;
    private final EmailCodeRepository emailCodeRepository;
    public DeleteEmailCode(EmailCode emailCode, EmailCodeRepository emailCodeRepository) {
        this.emailCode = emailCode;
        this.emailCodeRepository = emailCodeRepository;
    }
    @Override
    public void run(){
        try {
            emailCodeRepository.delete(emailCode);
        }catch (Exception e){
            throw new ServerError("More over one task !");
        }
    }
}
