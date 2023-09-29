package fpt.capstone.buildingmanagementsystem.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
@Builder(toBuilder = true)
public class User {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "user_id",updatable = false, nullable = false)
    String user_id;
    @NotNull
    @Column(name = "first_name")
    String first_name;
    @NotNull
    @Column(name = "last_name")
    String last_name;
    @NotNull
    @Column(name = "gender")
    String gender;
    @NotNull
    @Column(name = "date_of_birth")
    String date_of_birth;
    @NotNull
    @Column(name = "telephone_number")
    String telephone_number;
    @NotNull
    @Column(name = "country")
    String country;
    @NotNull
    @Column(name = "city")
    String city;
    @NotNull
    @Column(name = "email")
    String email;
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @Column(name = "created_date")
    public Date createdDate;
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @Column(name = "updated_date")
    public Date updatedDate;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private Account account;
}
