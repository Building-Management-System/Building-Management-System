package fpt.capstone.buildingmanagementsystem.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllUserInfoPending {
    String userId;
    String firstName;
    String lastName;
    String gender;
    String dateOfBirth;
    String telephoneNumber;
    String country;
    String city;
    String email;
    String image;
}
