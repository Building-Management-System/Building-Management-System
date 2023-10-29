package fpt.capstone.buildingmanagementsystem.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "accountId",updatable = false, nullable = false)
    public String accountId;
    @NotNull
    @Column(name = "username")
    public String username;
    @NotNull
    @Column(name = "password")
    public String password;
    @NotNull
    @Column(name = "created_date")
    public Date createdDate;
    @NotNull
    @Column(name = "updated_date")
    public Date updatedDate;
    @Column(name = "created_by")
    public String createdBy;
    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private User user;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "status_id")
    public Status status;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "role_id")
    public Role role;

}
