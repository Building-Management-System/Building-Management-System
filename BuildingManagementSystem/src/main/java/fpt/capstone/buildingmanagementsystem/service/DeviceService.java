package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.model.entity.Device;
import fpt.capstone.buildingmanagementsystem.model.entity.DeviceAccount;
import fpt.capstone.buildingmanagementsystem.model.entity.Room;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.ControlLogStatus;
import fpt.capstone.buildingmanagementsystem.model.request.DeviceRequest;
import fpt.capstone.buildingmanagementsystem.model.request.DeviceRoomRequest;
import fpt.capstone.buildingmanagementsystem.model.request.DeviceStatusRequest;
import fpt.capstone.buildingmanagementsystem.model.response.DeviceRoomResponse;
import fpt.capstone.buildingmanagementsystem.repository.AccountRepository;
import fpt.capstone.buildingmanagementsystem.repository.DeviceAccountRepository;
import fpt.capstone.buildingmanagementsystem.repository.DeviceRepository;
import fpt.capstone.buildingmanagementsystem.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
                .orElseThrow(() -> new BadRequest("Not_found_device"));

        if (request.getDeviceLcdId() != null) {
            device.setDeviceId(request.getDeviceLcdId());
        }

        if (request.getDeviceName() != null) {
            device.setDeviceName(request.getDeviceName());
        }
        if (request.getDeviceUrl() != null) {
            device.setDeviceUrl(request.getDeviceUrl());
        }

        Optional<Room> roomExist = roomRepository.getRoomByInActiveDevice(request.getNewRoomId());
        if (roomExist.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(roomExist.get());
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

                Optional<Room> roomOptional = roomRepository.getRoomByDevice(device.getId());
                if (roomOptional.isPresent()) {
                    Room room = roomOptional.get();
                    room.setDevice(null);
                    roomRepository.save(room);
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
}

