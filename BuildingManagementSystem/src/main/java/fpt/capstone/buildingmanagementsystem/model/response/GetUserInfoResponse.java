package fpt.capstone.buildingmanagementsystem.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class GetUserInfoResponse {
    String userName;
    Date hireDate;
    String firstName;
    String lastName;
    String gender;
    String dateOfBirth;
    String telephoneNumber;
    String country;
    String city;
    String email;
    String image;
    String departmentId;
    String departmentName;
}
