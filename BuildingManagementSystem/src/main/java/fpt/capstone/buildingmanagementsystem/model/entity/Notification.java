package fpt.capstone.buildingmanagementsystem.model.entity;

import fpt.capstone.buildingmanagementsystem.model.enumEnitty.NotificationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "Notification")
public class Notification {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    private String notificationId;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private Date createDate;

    @Column
    private Date updateDate;

    @Column
    private Date uploadDate;

    @Column
    private NotificationStatus notificationStatus;

    @Column
    private boolean priority;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User createdBy;
}
