package fpt.capstone.buildingmanagementsystem.model.entity;

import fpt.capstone.buildingmanagementsystem.model.enumEnitty.EvaluateEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class MonthlyEvaluate {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String evaluateId;

    @Column
    private double workingDay;

    @Column
    private double totalAttendance;

    @Column
    private int lateCheckin;

    @Column
    private int earlyCheckout;

    @Column
    private double permittedLeave;

    @Column
    private double nonPermittedLeave;

    @Column
    private int violate;

    @Column
    private double overTime;

    @Column
    private double paidDay;

    @Column
    private int month;

    @Column
    private int year;

    @Column
    @Enumerated(EnumType.STRING)
    private EvaluateEnum evaluateEnum;

    @Column
    private String note;

    @Column
    private Date createdDate;

    @Column
    private Date updateDate;

    @Column
    private Date approvedDate;

    @Column
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "acceptedBy")
    private User acceptedBy;

    @ManyToOne
    @JoinColumn(name = "createdBy")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "employee")
    private User employee;

}
