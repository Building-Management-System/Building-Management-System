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
import fpt.capstone.buildingmanagementsystem.model.entity.User;
import fpt.capstone.buildingmanagementsystem.model.entity.UserPending;
import fpt.capstone.buildingmanagementsystem.model.entity.UserPendingStatus;
import fpt.capstone.buildingmanagementsystem.model.request.AcceptChangeUserInfo;
import fpt.capstone.buildingmanagementsystem.model.request.ChangeUserInfoRequest;
import fpt.capstone.buildingmanagementsystem.model.request.GetUserInfoRequest;
import fpt.capstone.buildingmanagementsystem.model.response.GetAllUserInfoPending;
import fpt.capstone.buildingmanagementsystem.model.response.GetUserInfoResponse;
import fpt.capstone.buildingmanagementsystem.model.response.UserInfoResponse;
import fpt.capstone.buildingmanagementsystem.repository.AccountRepository;
import fpt.capstone.buildingmanagementsystem.repository.UserPendingRepository;
import fpt.capstone.buildingmanagementsystem.repository.UserPendingStatusRepository;
import fpt.capstone.buildingmanagementsystem.repository.UserRepository;
import fpt.capstone.buildingmanagementsystem.until.Until;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static fpt.capstone.buildingmanagementsystem.until.Until.generateRealTime;

@Service
public class UserManageService {
    @Autowired
    AccountRepository accountRepository;
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
                    changeUserInfoRequest.getDateOfBirth() != null &&
                    file != null
            ) {
                String[] subFileName = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
                List<String> stringList = new ArrayList<>(Arrays.asList(subFileName));
                String name = "avatar_" + UUID.randomUUID() + "." + stringList.get(1);
                Bucket bucket = StorageClient.getInstance().bucket();
                bucket.create(name, file.getBytes(), file.getContentType());
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
                String oldImage = user.get().getImage();
                Bucket bucket = StorageClient.getInstance().bucket();
                Blob blob = bucket.get(oldImage);
                if (blob != null) {
                    blob.delete();
                }
                Optional<UserPending> userPending = userPendingRepository.findById(userId);
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
        GetUserInfoResponse getUserInfoResponse = new GetUserInfoResponse();
        if (getUserInfoRequest.getUserId() != null) {
            Optional<User> user = userRepository.findByUserId(getUserInfoRequest.getUserId());
            if (!user.isPresent()) {
                throw new NotFound("user_not_found");
            }
            getUserInfoResponse = userMapper.convertGetUserInfo(user.get());
        } else {
            throw new BadRequest("request_fail");
        }
        return getUserInfoResponse;
    }

    public List<UserInfoResponse> getAllUserInfo() {
        List<UserInfoResponse> userInfoResponses = new ArrayList<>();
        List<User> users = userRepository.findAll();
        if(users.isEmpty()) return userInfoResponses;
        users.forEach(user -> {
            UserInfoResponse userInfoResponse = new UserInfoResponse();
            BeanUtils.copyProperties(user, userInfoResponse);
            userInfoResponse.setAccountId(user.getUserId());
            userInfoResponse.setRoleName(user.getAccount().getRole().getRoleName());
            userInfoResponses.add(userInfoResponse);
        });
        return userInfoResponses;
    }

}
