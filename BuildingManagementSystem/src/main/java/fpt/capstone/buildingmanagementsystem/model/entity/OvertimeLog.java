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
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
@Entity(name = "overtimeLog")
public class OvertimeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long otId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "startTime")
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "endTime")
    private Date endTime;

    @Column(name = "dateType")
    @Enumerated(EnumType.STRING)
    private DateType dateType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "manualStart")
    private Date manualStart;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "manualEnd")
    private Date manualEnd;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;
}
