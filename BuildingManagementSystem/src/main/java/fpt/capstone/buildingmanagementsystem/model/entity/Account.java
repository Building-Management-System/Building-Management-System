package fpt.capstone.buildingmanagementsystem.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account")
public class Account {
    @Id
    @Column(name = "account_id")
    public String accountId;
    @Column(name = "username")
    public String username;
    @Column(name = "password")
    public String password;
    @Temporal(TemporalType.TIMESTAMP)

    @Column(name = "created_date")
    public Date createdDate;
    @Temporal(TemporalType.TIMESTAMP)

    @Column(name = "updated_date")
    public Date updatedDate;
    @ManyToOne
    @JoinColumn(name = "status_id")
    public Status status;
    @ManyToOne
    @JoinColumn(name = "role_id")
    public Role role;
}
