package fpt.capstone.buildingmanagementsystem.repository;

import fpt.capstone.buildingmanagementsystem.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,String> {
    Optional<Account> findByUsername(String username);
    Optional<Account> findByAccountId(String accountId);

    Boolean existsByUsername(String username);
    @Transactional
    @Modifying
    @Query(value = "UPDATE account SET password = :password,updated_date = :updated_date where username = :username", nativeQuery = true)
    int updatePassword(@Param(value = "password") String password, @Param(value = "updated_date") Date updated_date, @Param(value = "username") String username);
    @Transactional
    @Modifying
    @Query(value = "UPDATE account SET status_id = :status_id where account_id = :account_id", nativeQuery = true)
    int updateStatusAccount(@Param(value = "status_id") String status_id, @Param(value = "account_id") String account_id);
    @Transactional
    @Modifying
    @Query(value = "UPDATE account SET role_id = :role_id where account_id = :account_id", nativeQuery = true)
    int updateRoleAccount(@Param(value = "role_id") String role_id, @Param(value = "account_id") String account_id);
}
