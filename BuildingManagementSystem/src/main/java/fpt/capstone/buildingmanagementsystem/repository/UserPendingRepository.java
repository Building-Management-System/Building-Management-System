package fpt.capstone.buildingmanagementsystem.repository;

import fpt.capstone.buildingmanagementsystem.model.entity.UserPending;
import fpt.capstone.buildingmanagementsystem.model.entity.UserPendingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserPendingRepository extends JpaRepository<UserPending,String> {
    @Transactional
    @Modifying
    @Query(value = "UPDATE user_pending SET first_name = :first_name" +
            ",last_name = :last_name" + ",gender = :gender" +
            ",date_of_birth = :date_of_birth" + ",telephone_number = :telephone_number" +
            ",country = :country" + ",city = :city" +
            ",email = :email" + ",created_date = :created_date" +
            ",image = :image" +",user_pending_status_id = :user_pending_status_id"+
            " where user_pending_id = :user_pending_id", nativeQuery = true)
    int updateUserInfo(@Param(value = "first_name") String first_name, @Param(value = "last_name") String last_name,
                       @Param(value = "gender") String gender, @Param(value = "date_of_birth") String date_of_birth,
                       @Param(value = "telephone_number") String telephone_number, @Param(value = "country") String country,
                       @Param(value = "city") String city, @Param(value = "email") String email, @Param(value = "image") String image
            , @Param(value = "created_date") Date created_date, @Param(value = "user_pending_status_id") String user_pending_status_id, @Param(value = "user_pending_id") String user_pending_id);
    @Transactional
    @Modifying
    @Query(value = "UPDATE user_pending SET user_pending_status_id = :user_pending_status_id"+
            " where user_pending_id = :user_pending_id", nativeQuery = true)
    int updateStatus( @Param(value = "user_pending_status_id") String user_pending_status_id, @Param(value = "user_pending_id") String user_pending_id);

    List<UserPending> findAllByUserPendingStatus(UserPendingStatus status);
}
