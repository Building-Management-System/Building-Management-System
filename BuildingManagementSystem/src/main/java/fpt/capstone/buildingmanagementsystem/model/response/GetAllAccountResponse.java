package fpt.capstone.buildingmanagementsystem.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetAllAccountResponse {
    String accountId;
    String username;
    String roleName;
    String statusName;
    String statusId;
    String createdBy;
}
