package fpt.capstone.buildingmanagementsystem.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeUserInfoRequest {
    String user_id;
    String first_name;
    String last_name;
    String gender;
    String date_of_birth;
    String telephone_number;
    String country;
    String city;
    String email;
}
