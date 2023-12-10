package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.exception.NotFound;
import fpt.capstone.buildingmanagementsystem.model.entity.Account;
import fpt.capstone.buildingmanagementsystem.model.entity.Device;
import fpt.capstone.buildingmanagementsystem.model.entity.DeviceAccount;
import fpt.capstone.buildingmanagementsystem.model.entity.Room;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.ControlLogStatus;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.DeviceStatus;
import fpt.capstone.buildingmanagementsystem.model.request.AccountDeviceRequest;
import fpt.capstone.buildingmanagementsystem.model.request.DeviceRequest;
import fpt.capstone.buildingmanagementsystem.model.request.DeviceRoomRequest;
import fpt.capstone.buildingmanagementsystem.model.request.DeviceStatusRequest;
import fpt.capstone.buildingmanagementsystem.model.response.AccountLcdResponse;
import fpt.capstone.buildingmanagementsystem.model.response.DeviceAccountResponse;
import fpt.capstone.buildingmanagementsystem.model.response.DeviceRoomResponse;
import fpt.capstone.buildingmanagementsystem.model.response.RoomResponse;
import fpt.capstone.buildingmanagementsystem.repository.AccountRepository;
import fpt.capstone.buildingmanagementsystem.repository.DeviceAccountRepository;
import fpt.capstone.buildingmanagementsystem.repository.DeviceRepository;
import fpt.capstone.buildingmanagementsystem.repository.RoomRepository;
import fpt.capstone.buildingmanagementsystem.until.Until;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeviceService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    DeviceAccountRepository deviceAccountRepository;

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    RoomRepository roomRepository;

    public List<DeviceRoomResponse> getAllDevice() {
        List<Room> rooms = roomRepository.findAll();
        return rooms.stream()
                .map(room -> new DeviceRoomResponse(
                        room.getRoomId(),
                        room.getRoomName(),
                        room.getDevice().getId(),
                        room.getDevice().getDeviceId(),
                        room.getDevice().getDeviceName(),
                        room.getDevice().getStatus(),
                        room.getDevice().getDeviceUrl(),
                        room.getDevice().getDeviceNote(),
                        room.getDevice().getUpdateDate()
                ))
                .collect(Collectors.toList());
    }

    public ResponseEntity<?> updateDevice(DeviceRoomRequest request) {
        Device device = deviceRepository.findById(request.getDeviceId())
                .orElseThrow(() -> new NotFound("Not_found_device"));

        if (!device.getStatus().equals(DeviceStatus.INACTIVE)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(device);
        }

        if (request.getDeviceLcdId() != null) {
            device.setDeviceId(request.getDeviceLcdId());
        }

        if (request.getDeviceName() != null) {
            device.setDeviceName(request.getDeviceName());
        }
        if (request.getDeviceUrl() != null) {
            device.setDeviceUrl(request.getDeviceUrl());
        }

        List<Room> roomExist = roomRepository.getRoomByInActiveDevice(request.getNewRoomId());
        if (!roomExist.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(device);
        }
        Room room = roomRepository.findById(request.getNewRoomId())
                .orElseThrow(() -> new BadRequest("Not_found_room"));

        room.setDevice(device);
        try {
            deviceRepository.save(device);
            roomRepository.save(room);
            return ResponseEntity.ok(room);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getCause());
        }
    }

    public ResponseEntity<?> updateDeviceStatus(DeviceStatusRequest request) {
        Device device = deviceRepository.findById(request.getId())
                .orElseThrow(() -> new BadRequest("Not_found_device"));
        device.setStatus(request.getStatus());
        device.setDeviceNote(request.getDeviceNote());
        try {
            return ResponseEntity.ok(deviceRepository.save(device));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getCause());
        }
    }

    @Deprecated
    public ResponseEntity<?> deleteDevice(DeviceRequest deviceRequest) {
        try {
            Device device = deviceRepository.findById(deviceRequest.getDeviceId())
                    .orElseThrow(() -> new BadRequest("Not_found_device"));
            List<DeviceAccount> deviceAccounts = deviceAccountRepository.findByDevice(device);

            List<DeviceAccount> whiteListAccount = deviceAccounts.stream()
                    .filter(deviceAccount -> deviceAccount.getStatus().equals(ControlLogStatus.WHITE_LIST))
                    .collect(Collectors.toList());
            if (whiteListAccount.isEmpty()) {
                //delete device account
                if (!deviceAccounts.isEmpty()) {
                    deviceAccountRepository.deleteAll(deviceAccounts);
                }

                List<Room> rooms = roomRepository.getRoomByDevice(device.getId());
                if (rooms.isEmpty()) {
//                    Room room = rooms.get();
//                    room.setDevice(null);
                    roomRepository.saveAll(rooms);
                }
                deviceRepository.delete(device);
                return ResponseEntity.ok(device);
            }
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(whiteListAccount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getCause());
        }
    }

    public DeviceAccountResponse getDeviceDetail(String deviceId) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new BadRequest("Not_found_device"));

        DeviceAccountResponse response = DeviceAccountResponse.builder()
                .deviceId(device.getId())
                .deviceLcdId(device.getDeviceId())
                .deviceName(device.getDeviceName())
                .createdDate(device.getCreatedDate())
                .status(device.getStatus())
                .build();

        List<Room> rooms = roomRepository.getRoomByDevice(device.getId());
        if (!rooms.isEmpty()) {
            List<RoomResponse> roomResponses = rooms.stream()
                    .map(room -> new RoomResponse(room.getRoomId(), room.getRoomName()))
                    .collect(Collectors.toList());
            response.setRooms(roomResponses);
        }
        List<AccountLcdResponse> deviceAccounts = deviceAccountRepository.findByDevice(device)
                .stream()
                .map(deviceAccount -> new AccountLcdResponse(
                        deviceAccount.getAccount().accountId,
                        deviceAccount.getAccount().getUsername(),
                        deviceAccount.getAccount().getUser().getFirstName(),
                        deviceAccount.getAccount().getUser().getFirstName(),
                        deviceAccount.getAccount().getUser().getDepartment(),
                        deviceAccount.getStartDate(),
                        deviceAccount.getEndDate(),
                        deviceAccount.getStatus()
                ))
                .collect(Collectors.toList());

        response.setAccountLcdResponses(deviceAccounts);
        return response;
    }

    public ResponseEntity<?> registerNewAccount(AccountDeviceRequest request) {
        try {
            if (request.getAccountId() != null
                    || request.getRoomIdString() != null
                    || request.getStartDate() != null) {
                Account account = accountRepository.findById(request.getAccountId())
                        .orElseThrow(() -> new BadRequest("Not_found_user"));

                int roomId = Integer.parseInt(request.getRoomIdString());
                Room room = roomRepository.findById(roomId)
                        .orElseThrow(() -> new BadRequest("Not_found_room"));
                java.util.Date fromDate = Until.convertStringToDateTime(request.getStartDate());

                java.util.Date toDate = null;
                if (request.getEndDate() != null) {
                    toDate = Until.convertStringToDateTime(request.getEndDate());
                    if (fromDate.compareTo(toDate) > 0) {
                        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                                .body(request);
                    }
                }
                DeviceAccount deviceAccount = DeviceAccount.builder()
                        .startDate(fromDate)
                        .createdDate(Until.generateRealTime())
                        .updateDate(Until.generateRealTime())
                        .status(ControlLogStatus.WHITE_LIST)
                        .device(room.getDevice())
                        .account(account)
                        .build();
                if (toDate != null) deviceAccount.setEndDate(toDate);
                DeviceAccount saveTo = deviceAccountRepository.save(deviceAccount);
                return ResponseEntity.ok(messageSetupMqtt(saveTo.getAccount().getAccountId(), request.getStartDate(), request.getEndDate(), saveTo.getDevice().getDeviceId()));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(request);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getCause());
        }
    }

//    public ResponseEntity<?> changeAccountStatus()

    public String messageSetupMqtt(String accountId, String startDate, String endDate, String deviceLcdId) {
        return "{\n" +
                "\"messageId\":\"AddPersonslist2020-04-13T19:07:00_00001\",\n" +
                " \"DataBegin\":\"BeginFlag\",\n" +
                " \"operator\":\"AddPersons\", \n" +
                "\"PersonNum\":\"1000\", \n" +
                "\"info\":\n" +
                "[\n" +
                "\t{\n" +
                "\t\t\"customId\":\"" + accountId + "\", \n" +
                "\t\t\"name\":\"test000\",\n" +
                "\t\t\"nation\":1,\n" +
                "\t\t\"gender\":0,\n" +
                "\t\t\"birthday\":\"1992-06-13\", \n" +
                "\t\t\"address\":\" Baoan District, Shenzhen City, Guangdong Province \",\n" +
                "\t\t\"idCard\":\"" + deviceLcdId + "\", \n" +
                "\t\t\"tempCardType\":0, \n" +
                "\t\t\"EffectNumber\":3,\n" +
                "\t\t \"cardValidBegin\":\"" + startDate + "\", \n" +
                "\t\t\"cardValidEnd\":\"" + endDate + "\", \n" +
                "\t\t\"telnum1\":\"13690880000\",\n" +
                "\t\t \"native\":\" Shenzhen , Guangdong \", \n" +
                "\t\t\"cardType2\":0, \"notes\":\"\",\n" +
                "\t\t \"personType\":0, \n" +
                "\t\t\"cardType\":0,\n" +
                "\t\t\"picURI\":\"https://btgongpluss.oss-cn-beijing.aliyuncs.com/bigheadphoto/xxx.jpg\"\n" +
                "\t }\n" +
                "]\n" +
                "\n" +
                " \"DataEnd\":\"EndFlag\" \n" +
                "}\n";
    }
}

