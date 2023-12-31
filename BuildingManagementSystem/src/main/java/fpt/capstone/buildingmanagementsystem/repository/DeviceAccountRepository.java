package fpt.capstone.buildingmanagementsystem.repository;

import fpt.capstone.buildingmanagementsystem.model.entity.Account;
import fpt.capstone.buildingmanagementsystem.model.entity.Device;
import fpt.capstone.buildingmanagementsystem.model.entity.DeviceAccount;
import fpt.capstone.buildingmanagementsystem.model.entity.EmailCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface DeviceAccountRepository extends JpaRepository<DeviceAccount, String> {
    List<DeviceAccount> findByDevice(Device device);

    List<DeviceAccount> findByDeviceAndAccount(Device device, Account account);

    List<DeviceAccount> findByAccount(Account account);
    List<DeviceAccount> findAllByInTaskDelete(boolean check);
}
