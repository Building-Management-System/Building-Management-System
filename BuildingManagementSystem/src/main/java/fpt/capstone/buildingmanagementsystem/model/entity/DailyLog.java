package fpt.capstone.buildingmanagementsystem.model.entity;

import fpt.capstone.buildingmanagementsystem.model.enumEnitty.DateType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.Instant;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
@Entity(name = "dailyLog")
public class DailyLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dailyId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dayTime")
    private Date dayTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "firstEntry")
    private Date firstEntry;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "lastExit")
    private Instant lastExit;

    @Column(name = "morningTotal")
    private int morningTotal;

    @Column(name = "afternoonTotal")
    private int afternoonTotal;

    @Column(name = "permittedLeave")
    private int permittedLeave;

    @Column(name = "nonPermittedLeave")
    private int nonPermittedLeave;

    @Column(name = "dateType")
    @Enumerated(EnumType.STRING)
    private DateType dateType;


    @Column(name = "otStatus")
    private boolean otStatus;

    @Column(name = "totalTime")
    private int totalTime;

    @Column(name = "outsideTime")
    private int outsideTime;

    @Column(name = "insideTime")
    private int insideTime;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
