package fpt.capstone.buildingmanagementsystem.service.schedule;

import fpt.capstone.buildingmanagementsystem.exception.ServerError;
import fpt.capstone.buildingmanagementsystem.model.entity.DeviceAccount;
import fpt.capstone.buildingmanagementsystem.repository.DeviceAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class AutoDeleteAccountDeviceService {
    @Autowired
    DeviceAccountRepository deviceAccountRepository;
    @Scheduled(cron = "0 0/2 * * * ?")
    public void getAllNotificationScheduled(){
        List<DeviceAccount> deviceAccountList= deviceAccountRepository.findAllByInTaskDelete(false);
        if(deviceAccountList.size()>0) {
            for (DeviceAccount deviceAccount : deviceAccountList) {
                if (!(deviceAccount.getEndDate() == null)) {
                    Timer timer = new Timer();
                    Date scheduledTime = new Date();
                    scheduledTime.setTime(deviceAccount.getEndDate().getTime());
                    DeleteAccountDevice uploadDate = new DeleteAccountDevice(deviceAccount, deviceAccountRepository);
                    timer.schedule(uploadDate, scheduledTime);
                    deviceAccount.setInTaskDelete(true);
                    deviceAccountRepository.save(deviceAccount);
                }
            }
        }
    }
}
class DeleteAccountDevice extends TimerTask {
    private final DeviceAccount deviceAccount;
    private final DeviceAccountRepository deviceAccountRepository;
    public DeleteAccountDevice(DeviceAccount deviceAccount,DeviceAccountRepository deviceAccountRepository) {
        this.deviceAccount = deviceAccount;
        this.deviceAccountRepository = deviceAccountRepository;
    }
    @Override
    public void run() {
        try {
            deviceAccountRepository.delete(deviceAccount);
        }catch (Exception e){
            throw new ServerError("More over one task !");
        }
    }
}

