package fpt.capstone.buildingmanagementsystem.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ControlLogByAccountResponse {
    String controlLogId;
    byte[] image;
    String account;
    String firstName;
    String lastName;
    String department;
    String timeRecord;
    String verifyType;
    String room;
}
