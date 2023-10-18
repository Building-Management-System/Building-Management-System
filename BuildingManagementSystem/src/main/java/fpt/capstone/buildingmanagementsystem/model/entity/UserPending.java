package fpt.capstone.buildingmanagementsystem.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_pending")
public class UserPending {
    @Id
    @Column(name = "user_pending_id")
    String userId;
    @NotNull
    @Column(name = "first_name")
    String firstName;
    @NotNull
    @Column(name = "last_name")
    String lastName;
    @NotNull
    @Column(name = "gender")
    String gender;
    @NotNull
    @Column(name = "date_of_birth")
    String dateOfBirth;
    @NotNull
    @Column(name = "telephone_number")
    String telephoneNumber;
    @NotNull
    @Column(name = "country")
    String country;
    @NotNull
    @Column(name = "city")
    String city;
    @NotNull
    @Column(name = "email")
    String email;
    @NotNull
    @Column(name = "image")
    String image;
    @NotNull
    @Column(name = "created_date")
    String createdDate;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_pending_status_id")
    public UserPendingStatus userPendingStatus;
}
