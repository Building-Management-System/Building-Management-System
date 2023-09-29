package fpt.capstone.buildingmanagementsystem.repository;

import fpt.capstone.buildingmanagementsystem.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Transactional
    @Modifying
    @Query(value = "UPDATE user SET first_name = :first_name" +
            ",last_name = :last_name" + ",gender = :gender" +
            ",date_of_birth = :date_of_birth" + ",telephone_number = :telephone_number" +
            ",country = :country" + ",city = :city" +
            ",email = :email" + ",updated_date = :updated_date" +
            ",image = :image" + ",image = :image" +
            " where user_id = :user_id", nativeQuery = true)
    int updateUserInfo(@Param(value = "first_name") String first_name, @Param(value = "last_name") String last_name,
                       @Param(value = "gender") String gender, @Param(value = "date_of_birth") String date_of_birth,
                       @Param(value = "telephone_number") String telephone_number, @Param(value = "country") String country,
                       @Param(value = "city") String city, @Param(value = "email") String email, @Param(value = "image") String image
            , @Param(value = "updated_date") Date updated_date, @Param(value = "user_id") String user_id);
}
