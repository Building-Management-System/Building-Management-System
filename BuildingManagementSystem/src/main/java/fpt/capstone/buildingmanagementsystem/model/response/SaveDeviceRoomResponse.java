package fpt.capstone.buildingmanagementsystem.model.response;

import fpt.capstone.buildingmanagementsystem.model.enumEnitty.DeviceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class SaveDeviceRoomResponse {

    private String deviceId;

    private String deviceLcdId;

    private String deviceName;

    private DeviceStatus status;

    private String deviceUrl;

    private int roomId;

    private String roomName;
}
