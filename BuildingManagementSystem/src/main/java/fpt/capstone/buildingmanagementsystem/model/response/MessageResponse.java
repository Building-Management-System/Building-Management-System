package fpt.capstone.buildingmanagementsystem.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MessageResponse {
    private boolean myself;
    private String message;
    private String createdAt;
    private String type;
}
