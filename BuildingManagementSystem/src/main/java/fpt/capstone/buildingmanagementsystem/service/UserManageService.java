package fpt.capstone.buildingmanagementsystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.*;
import com.google.firebase.cloud.StorageClient;
import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.exception.NotFound;
import fpt.capstone.buildingmanagementsystem.exception.ServerError;
import fpt.capstone.buildingmanagementsystem.mapper.UserMapper;
import fpt.capstone.buildingmanagementsystem.model.request.ChangeUserInfoRequest;
import fpt.capstone.buildingmanagementsystem.repository.AccountRepository;
import fpt.capstone.buildingmanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.*;

import static fpt.capstone.buildingmanagementsystem.until.Until.generateRealTime;

@Service
public class UserManageService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserMapper userMapper;

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
                if (!userRepository.existsById(userId)) {
                    throw new NotFound("user_not_found");
                }
                String[] subFileName = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
                List<String> stringList = new ArrayList<>(Arrays.asList(subFileName));
                String username = accountRepository.findByAccountId(userId).get().getUsername();
                String name = username + "." + stringList.get(1);
                String oldImage=userRepository.findByUserId(userId).get().getImage();
                Bucket bucket = StorageClient.getInstance().bucket();
                Blob blob = bucket.get(oldImage);
                if (blob != null) {
                    blob.delete();
                }
                bucket.create(name, file.getBytes(), file.getContentType());
                userRepository.updateUserInfo(changeUserInfoRequest.getFirstName(),
                        changeUserInfoRequest.getLastName(), changeUserInfoRequest.getGender(), changeUserInfoRequest.getDateOfBirth()
                        , changeUserInfoRequest.getTelephoneNumber(), changeUserInfoRequest.getCountry()
                        , changeUserInfoRequest.getCity(), changeUserInfoRequest.getEmail(), name, generateRealTime(), changeUserInfoRequest.getUserId());
                return true;
            } else {
                throw new BadRequest("request_fail");
            }
        } catch (Exception e) {
            throw new ServerError("fail");
        }
    }

    public Object getUserInfo(String username) throws IOException {
        return null;
    }
}
