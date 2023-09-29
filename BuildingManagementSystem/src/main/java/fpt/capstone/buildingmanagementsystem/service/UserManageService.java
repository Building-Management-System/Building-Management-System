package fpt.capstone.buildingmanagementsystem.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.exception.NotFound;
import fpt.capstone.buildingmanagementsystem.exception.ServerError;
import fpt.capstone.buildingmanagementsystem.mapper.UserMapper;
import fpt.capstone.buildingmanagementsystem.model.request.ChangeUserInfoRequest;
import fpt.capstone.buildingmanagementsystem.repository.AccountRepository;
import fpt.capstone.buildingmanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import static fpt.capstone.buildingmanagementsystem.until.Until.generateRealTime;

@Service
public class UserManageService {
    @Value("${upload.path}")
    private String fileUpload;
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
                    changeUserInfoRequest.getImage()!=null &&
                    changeUserInfoRequest.getDate_of_birth()!=null&&
                    file !=null
            ) {
                if (!userRepository.existsById(userId)) {
                    throw new NotFound("user_not_found");
                }
                String[] subFileName = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
                List<String> stringList = new ArrayList<>(Arrays.asList(subFileName));
                String username=accountRepository.findByAccountId(userId).get().getUsername();
                String fileName = username+"."+stringList.get(1);
                File convertFile= new File(fileUpload+fileName);
                File dir = new File(fileUpload);
                String[] list = dir.list();
                for (int i = 0; i < list.length; i++) {
                    String[] subOldFileName = Objects.requireNonNull(list[i]).split("\\.");
                    List<String> checkOldFile = new ArrayList<>(Arrays.asList(subOldFileName));
                    if(username.equals(checkOldFile.get(0))){
                        File oldFile = new File(fileUpload+list[i]);
                        oldFile.delete();
                    }
                }
                convertFile.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(convertFile);
                fileOutputStream.write(file.getBytes());
                fileOutputStream.close();
                try {
                    FileCopyUtils.copy(file.getBytes(), new File(this.fileUpload + fileName));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                userRepository.updateUserInfo(changeUserInfoRequest.getFirst_name(),
                        changeUserInfoRequest.getLast_name(), changeUserInfoRequest.getGender(), changeUserInfoRequest.getDate_of_birth()
                        ,changeUserInfoRequest.getTelephone_number(),changeUserInfoRequest.getCountry()
                        ,changeUserInfoRequest.getCity(),changeUserInfoRequest.getEmail(),fileName,generateRealTime(),changeUserInfoRequest.getUser_id());
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
