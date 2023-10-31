package fpt.capstone.buildingmanagementsystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.exception.NotFound;
import fpt.capstone.buildingmanagementsystem.exception.ServerError;
import fpt.capstone.buildingmanagementsystem.mapper.UserMapper;
import fpt.capstone.buildingmanagementsystem.mapper.UserPendingMapper;
import fpt.capstone.buildingmanagementsystem.model.entity.Account;
import fpt.capstone.buildingmanagementsystem.model.entity.Department;
import fpt.capstone.buildingmanagementsystem.model.entity.User;
import fpt.capstone.buildingmanagementsystem.model.entity.UserPending;
import fpt.capstone.buildingmanagementsystem.model.entity.UserPendingStatus;
import fpt.capstone.buildingmanagementsystem.model.request.AcceptChangeUserInfo;
import fpt.capstone.buildingmanagementsystem.model.request.ChangeUserInfoRequest;
import fpt.capstone.buildingmanagementsystem.model.request.GetUserInfoRequest;
import fpt.capstone.buildingmanagementsystem.model.response.GetAllUserInfoPending;
import fpt.capstone.buildingmanagementsystem.model.response.GetUserInfoResponse;
import fpt.capstone.buildingmanagementsystem.model.response.HrDepartmentResponse;
import fpt.capstone.buildingmanagementsystem.model.response.ManagerInfoResponse;
import fpt.capstone.buildingmanagementsystem.model.response.ReceiveIdAndDepartmentIdResponse;
import fpt.capstone.buildingmanagementsystem.model.response.UserAccountResponse;
import fpt.capstone.buildingmanagementsystem.model.response.UserInfoResponse;
import fpt.capstone.buildingmanagementsystem.repository.AccountRepository;
import fpt.capstone.buildingmanagementsystem.repository.DepartmentRepository;
import fpt.capstone.buildingmanagementsystem.repository.RoleRepository;
import fpt.capstone.buildingmanagementsystem.repository.UserPendingRepository;
import fpt.capstone.buildingmanagementsystem.repository.UserPendingStatusRepository;
import fpt.capstone.buildingmanagementsystem.repository.UserRepository;
import fpt.capstone.buildingmanagementsystem.repository.UserRepositoryV2;
import fpt.capstone.buildingmanagementsystem.service.schedule.TicketRequestScheduledService;
import fpt.capstone.buildingmanagementsystem.until.Until;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static fpt.capstone.buildingmanagementsystem.until.Until.generateRealTime;

@Service
public class UserManageService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    UserPendingRepository userPendingRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserPendingMapper userPendingMapper;
    @Autowired
    UserPendingStatusRepository userPendingStatusRepository;

    @Autowired
    UserRepositoryV2 userRepositoryV2;

    private static final Logger logger = LoggerFactory.getLogger(TicketRequestScheduledService.class);


    public boolean ChangeUserInfo(String data, MultipartFile file) {
        try {
            ChangeUserInfoRequest changeUserInfoRequest = new ObjectMapper().readValue(data, ChangeUserInfoRequest.class);
            String userId = changeUserInfoRequest.getUserId();
            if (userId != null && changeUserInfoRequest.getFirstName() != null &&
                    changeUserInfoRequest.getLastName() != null &&
                    changeUserInfoRequest.getCountry() != null &&
                    changeUserInfoRequest.getEmail() != null &&
                    changeUserInfoRequest.getGender() != null &&
                    changeUserInfoRequest.getCity() != null &&
                    changeUserInfoRequest.getTelephoneNumber() != null &&
                    changeUserInfoRequest.getDateOfBirth() != null
            ) {
                String name = "avatar_" + UUID.randomUUID();
                if (file != null) {
                    String[] subFileName = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
                    List<String> stringList = new ArrayList<>(Arrays.asList(subFileName));
                    name = name + "." + stringList.get(stringList.size() - 1);
                    Bucket bucket = StorageClient.getInstance().bucket();
                    bucket.create(name, file.getBytes(), file.getContentType());
                } else {
                    name = userRepository.findByUserId(changeUserInfoRequest.getUserId()).get().getImage();
                }
                if (!userPendingRepository.existsById(userId)) {
                    Optional<UserPendingStatus> userPendingStatus = userPendingStatusRepository.findByUserPendingStatusId("1");
                    UserPending userPending = userPendingMapper.convertRegisterAccount(changeUserInfoRequest);
                    userPending.setImage(name);
                    userPending.setUserPendingStatus(userPendingStatus.get());
                    userPendingRepository.save(userPending);
                } else {
                    userPendingRepository.updateUserInfo(changeUserInfoRequest.getFirstName(),
                            changeUserInfoRequest.getLastName(), changeUserInfoRequest.getGender(), changeUserInfoRequest.getDateOfBirth()
                            , changeUserInfoRequest.getTelephoneNumber(), changeUserInfoRequest.getCountry()
                            , changeUserInfoRequest.getCity(), changeUserInfoRequest.getEmail(), name, Until.generateRealTime(), "1", changeUserInfoRequest.getUserId());
                }
                return true;
            } else {
                throw new BadRequest("request_fail");
            }
        } catch (Exception e) {
            throw new ServerError("fail");
        }
    }


    public boolean AcceptChangeUserInfo(AcceptChangeUserInfo acceptChangeUserInfo) {
        try {
            String userId = acceptChangeUserInfo.getUserId();
            if (userId != null) {
                if (!userRepository.existsById(userId)) {
                    throw new NotFound("user_not_found");
                }
                Optional<User> user = userRepository.findByUserId(userId);
                Optional<UserPending> userPending = userPendingRepository.findById(userId);
                if (!userPending.isPresent() || !user.isPresent()) {
                    throw new NotFound("not_found");
                }
                String oldImage = user.get().getImage();
                String newImage = userPending.get().getImage();
                if (!Objects.equals(newImage, oldImage)) {
                    Bucket bucket = StorageClient.getInstance().bucket();
                    Blob blob = bucket.get(oldImage);
                    if (blob != null) {
                        blob.delete();
                    }
                }
                userRepository.updateAcceptUserInfo(userPending.get().getFirstName(), userPending.get().getLastName(), userPending.get().getGender()
                        , userPending.get().getDateOfBirth(), userPending.get().getTelephoneNumber()
                        , userPending.get().getCity(), userPending.get().getCity(), userPending.get().getEmail()
                        , userPending.get().getImage(), generateRealTime()
                        , userId);
                userPendingRepository.updateStatus("2", userId);
                return true;
            } else {
                throw new BadRequest("request_fail");
            }
        } catch (ServerError e) {
            throw new ServerError("fail");
        }
    }

    public boolean RejectChangeUserInfo(GetUserInfoRequest getUserInfoRequest) {
        try {
            if (getUserInfoRequest.getUserId() != null) {
                if (userPendingRepository.existsById(getUserInfoRequest.getUserId())) {
                    userPendingRepository.updateStatus("3", getUserInfoRequest.getUserId());
                    return true;
                } else {
                    throw new NotFound("user_not_found");
                }
            } else {
                throw new BadRequest("request_fail");
            }
        } catch (ServerError e) {
            throw new ServerError("fail");
        }
    }

    public List<GetAllUserInfoPending> getAllUserNotVerify() {
        UserPendingStatus status = new UserPendingStatus("1", "not_verify");
        List<GetAllUserInfoPending> listResponse = new ArrayList<>();
        List<UserPending> userPending = userPendingRepository.findAllByUserPendingStatus(status);
        if (userPending.size() == 0) {
            return listResponse;
        }
        userPending.forEach(element -> listResponse.add(userPendingMapper.convertGetUserInfoPending(element)));
        return listResponse;
    }

    public GetUserInfoResponse getInfoUser(GetUserInfoRequest getUserInfoRequest) {
        GetUserInfoResponse getUserInfoResponse;
        if (getUserInfoRequest.getUserId() != null) {
            Optional<User> user = userRepository.findByUserId(getUserInfoRequest.getUserId());
            if (!user.isPresent()) {
                throw new NotFound("user_not_found");
            }
            getUserInfoResponse = userMapper.convertGetUserInfo(user.get());
            getUserInfoResponse.setDepartmentName(user.get().getDepartment().getDepartmentName());
        } else {
            throw new BadRequest("request_fail");
        }
        return getUserInfoResponse;
    }

    public List<UserInfoResponse> getAllUserInfo() {
        List<UserInfoResponse> userInfoResponses = new ArrayList<>();
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) return userInfoResponses;
        return getUserInfoResponses(userInfoResponses, users);
    }

    public ReceiveIdAndDepartmentIdResponse getReceiveIdAndDepartmentId(String userId) {
        if (userId != null) {
            if (userRepository.findByUserId(userId).isPresent()) {
                Department department = userRepository.findByUserId(userId).get().getDepartment();
                List<User> userOfDepartment = userRepository.findAllByDepartment(department);
                String managerId = null;
                for (int i = 0; i < userOfDepartment.size(); i++) {
                    Optional<Account> account = accountRepository.findByAccountId(userOfDepartment.get(i).getUserId());
                    if (Objects.equals(account.get().getRole().getRoleId(), "3")) {
                        managerId = account.get().getAccountId();
                    }
                }
                ManagerInfoResponse managerInfoResponse = ManagerInfoResponse.builder().managerDepartmentId(department.getDepartmentId())
                        .managerDepartmentName(department.getDepartmentName())
                        .managerId(managerId)
                        .build();
                Optional<Department> departmentHr = departmentRepository.findByDepartmentId("3");
                HrDepartmentResponse hrDepartmentResponse = HrDepartmentResponse.builder().hrDepartmentId(departmentHr.get().getDepartmentId())
                        .hrDepartmentName(departmentHr.get().getDepartmentName())
                        .build();
                return new ReceiveIdAndDepartmentIdResponse(managerInfoResponse, hrDepartmentResponse);
            } else {
                throw new NotFound("user_not_found");
            }
        } else {
            throw new BadRequest("request_fail");
        }
    }

    public List<UserInfoResponse> getManagerByDepartmentId(String departmentId) {
        List<UserInfoResponse> userInfoResponses = new ArrayList<>();
        List<User> users = userRepository.getManagerByDepartmentId(departmentId);
        logger.info("" + users.size());
        return getUserInfoResponses(userInfoResponses, users);
    }

    private List<UserInfoResponse> getUserInfoResponses(List<UserInfoResponse> userInfoResponses, List<User> users) {
        users.forEach(u -> {
            UserInfoResponse response = new UserInfoResponse();
            BeanUtils.copyProperties(u, response);
            response.setAccountId(u.getUserId());
            response.setRoleName(u.getAccount().getRole().getRoleName());
            userInfoResponses.add(response);
        });
        return userInfoResponses;
    }

    public List<UserAccountResponse> getUserAccount(String userId) {
        return userRepositoryV2.getUserAccount().stream()
                .filter(user -> !user.getAccountId().equals(userId))
                .collect(Collectors.toList());
    }
}
