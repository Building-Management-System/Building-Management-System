package fpt.capstone.buildingmanagementsystem.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@ToString
public class ControlLogLcd {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String controlLogId;

    @Column
    @JsonProperty("operator")
    private String operator;

    @Column
    @JsonProperty("personId")
    private String personId;

    @Column
    @JsonProperty("RecordID")
    private int recordId;

    @Column
    @JsonProperty("verifyStatus")
    private int verifyStatus;

    @Column
    @JsonProperty("similarity1")
    private double similarity1;

    @Column
    @JsonProperty("similarity2")
    private double similarity2;

    @Column
    @JsonProperty("persionName")
    private String persionName;

    @Column
    @JsonProperty("telnum")
    private String telnum;

    @Column
    @JsonProperty("time")
    private Date time;

    @Lob
    @Column
    @JsonProperty("pic")
    private byte[] pic;
}
