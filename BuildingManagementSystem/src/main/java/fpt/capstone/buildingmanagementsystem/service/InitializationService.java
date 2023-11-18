package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.mapper.AccountMapper;
import fpt.capstone.buildingmanagementsystem.model.entity.*;
import fpt.capstone.buildingmanagementsystem.model.request.RegisterRequest;
import fpt.capstone.buildingmanagementsystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static fpt.capstone.buildingmanagementsystem.until.Until.generateRealTime;

@Service
public class InitializationService {
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    StatusRepository statusRepository;
    @Autowired
    UserPendingStatusRepository userPendingStatusRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    DayOffRepository dayOffRepository;

    public void init() {
        int check1 = roleRepository.findAll().size();
        int check2 = statusRepository.findAll().size();
        int check3 = userPendingStatusRepository.findAll().size();
        int check4 = departmentRepository.findAll().size();
        int check5 = roomRepository.findAll().size();
        int check6 = accountRepository.findAll().size();
        int check7 = dayOffRepository.findAll().size();
        if (check1 == 0 & check2 == 0 && check3 == 0 && check4 == 0 && check5 == 0 && check6 == 0 && check7 == 0) {
            List<Role> roleList = new ArrayList<>();
            List<Status> statusList = new ArrayList<>();
            List<UserPendingStatus> userPendingStatusList = new ArrayList<>();
            List<Department> departmentList = new ArrayList<>();
            List<Room> roomList = new ArrayList<>();
            Role role1 = new Role("1", "hr");
            Role role2 = new Role("2", "admin");
            Role role3 = new Role("3", "manager");
            Role role4 = new Role("4", "security");
            Role role5 = new Role("5", "employee");
            roleList.add(role1);
            roleList.add(role2);
            roleList.add(role3);
            roleList.add(role4);
            roleList.add(role5);
            Status status1 = new Status("0", "inactive");
            Status status2 = new Status("1", "active");
            statusList.add(status1);
            statusList.add(status2);
            UserPendingStatus userPendingStatus1 = new UserPendingStatus("1", "not_verify");
            UserPendingStatus userPendingStatus2 = new UserPendingStatus("2", "verify");
            UserPendingStatus userPendingStatus3 = new UserPendingStatus("3", "reject");
            userPendingStatusList.add(userPendingStatus1);
            userPendingStatusList.add(userPendingStatus2);
            userPendingStatusList.add(userPendingStatus3);
            Department department1 = new Department("10", "security");
            Department department2 = new Department("2", "tech D1");
            Department department3 = new Department("3", "human resources");
            Department department4 = new Department("4", "tech D2");
            Department department5 = new Department("5", "tech D3");
            Department department6 = new Department("6", "tech D4");
            Department department7 = new Department("7", "tech D5");
            Department department8 = new Department("8", "tech D6");
            Department department9 = new Department("9", "Admin");
            departmentList.add(department1);
            departmentList.add(department2);
            departmentList.add(department3);
            departmentList.add(department4);
            departmentList.add(department5);
            departmentList.add(department6);
            departmentList.add(department7);
            departmentList.add(department8);
            departmentList.add(department9);
            for (int i = 1; i < 10; i++) {
                Room room = new Room(i, "R10" + i);
                roomList.add(room);
            }
            RegisterRequest registerRequest = new RegisterRequest("demo", "123", "hr", "human resources", "");
            Account newAccount = accountMapper.convertRegisterAccount(registerRequest, status2, role1);
            User user = User.builder().city("unknown").country("unknown").email("unknown").firstName("unknown")
                    .lastName("unknown").dateOfBirth("unknown").telephoneNumber("unknown").gender("unknown").createdDate(
                            generateRealTime()).image("unknown").updatedDate(generateRealTime()).department(department3)
                    .build();
            user.setAccount(newAccount);
            roleRepository.saveAll(roleList);
            statusRepository.saveAll(statusList);
            userPendingStatusRepository.saveAll(userPendingStatusList);
            departmentRepository.saveAll(departmentList);
            roomRepository.saveAll(roomList);
            userRepository.save(user);
            dayOffRepository.saveAll(initEmployeeDayOff());
        }
    }

    public List<DayOff> initEmployeeDayOff() {
        List<DayOff> dayOffs = new ArrayList<>();
        accountRepository.findAll()
                .forEach(account -> {
                    DayOff dayOff = DayOff.builder()
                            .account(account)
                            .january(48)
                            .february(48)
                            .april(48)
                            .march(48)
                            .may(48)
                            .july(48)
                            .june(48)
                            .august(48)
                            .september(48)
                            .october(48)
                            .november(48)
                            .december(48)
                            .year(2023)
                            .build();
                    dayOffs.add(dayOff);
                });
        return dayOffs;
    }
}
