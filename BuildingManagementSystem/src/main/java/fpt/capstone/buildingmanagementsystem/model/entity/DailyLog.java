package fpt.capstone.buildingmanagementsystem.model.entity;

import fpt.capstone.buildingmanagementsystem.model.enumEnitty.DateType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

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
import java.sql.Date;
import java.sql.Time;
import java.time.Instant;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
@Entity(name = "daily_log")
public class DailyLog {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "daily_log_id")
    private String dailyId;

    @Column(name = "date")
    private Date date;

    @Column(name = "first_entry")
    private Time firstEntry;

    @Column(name = "last_exit")
    private Time lastExit;

    @Column(name = "morning_total")
    private float morningTotal;

    @Column(name = "afternoon_total")
    private float afternoonTotal;

    @Column(name = "permitted_leave")
    private float permittedLeave;

    @Column(name = "non_permitted_leave")
    private float nonPermittedLeave;

    @Column(name = "date_type")
    @Enumerated(EnumType.STRING)
    private DateType dateType;

    @Column(name = "total_time")
    private float totalTime;

    @Column(name = "outside_time")
    private float outsideTime;

    @Column(name = "inside_time")
    private float insideTime;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
