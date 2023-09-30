package fpt.capstone.buildingmanagementsystem.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.exception.NotFound;
import fpt.capstone.buildingmanagementsystem.exception.ServerError;
import fpt.capstone.buildingmanagementsystem.firebase.UploadFile;
import fpt.capstone.buildingmanagementsystem.mapper.UserMapper;
import fpt.capstone.buildingmanagementsystem.model.request.ChangeUserInfoRequest;
import fpt.capstone.buildingmanagementsystem.repository.AccountRepository;
import fpt.capstone.buildingmanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
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
            String userId=changeUserInfoRequest.getUser_id();
            if (userId != null && changeUserInfoRequest.getFirst_name()!=null &&
                    changeUserInfoRequest.getLast_name()!=null &&
                    changeUserInfoRequest.getCountry()!=null &&
                    changeUserInfoRequest.getEmail()!=null &&
                    changeUserInfoRequest.getGender()!=null &&
                    changeUserInfoRequest.getCity()!=null &&
                    changeUserInfoRequest.getTelephone_number()!=null &&
                    changeUserInfoRequest.getDate_of_birth()!=null&&
                    file !=null
            ) {
                if (!userRepository.existsById(userId)) {
                    throw new NotFound("user_not_found");
                }
                String[] subFileName = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
                List<String> stringList = new ArrayList<>(Arrays.asList(subFileName));
                String username=accountRepository.findByAccountId(userId).get().getUsername();
                String name = username+"."+stringList.get(1);
                Bucket bucket = StorageClient.getInstance().bucket();
                Blob blob = bucket.get(name);
                if (blob != null) {
                    blob.delete();
                }
                bucket.create(name, file.getBytes(), file.getContentType());
                userRepository.updateUserInfo(changeUserInfoRequest.getFirst_name(),
                        changeUserInfoRequest.getLast_name(), changeUserInfoRequest.getGender(), changeUserInfoRequest.getDate_of_birth()
                        ,changeUserInfoRequest.getTelephone_number(),changeUserInfoRequest.getCountry()
                        ,changeUserInfoRequest.getCity(),changeUserInfoRequest.getEmail(),name,generateRealTime(),changeUserInfoRequest.getUser_id());
                return true;
            } else {
                throw new BadRequest("request_fail");
            }
        } catch (ServerError e) {
            throw new ServerError("fail");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
