package fpt.capstone.buildingmanagementsystem.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomBookingResponse {
    private String id;
    private String endDate;
    private String startDate;
    private String title;
    private String departmentId;
    private String bookingDate;
    private int roomId;
}
