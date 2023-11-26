package fpt.capstone.buildingmanagementsystem.repository;

import fpt.capstone.buildingmanagementsystem.model.entity.Chat;
import fpt.capstone.buildingmanagementsystem.model.entity.ChatUser;
import fpt.capstone.buildingmanagementsystem.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ChatUserRepository extends JpaRepository<ChatUser, String> {
    List<ChatUser> findAllByChat_Id(String chatId);
    List<ChatUser> findAllByUser_UserIdIsNot(String userId);
    List<ChatUser> findAllByUser_UserId(String userId);
    void deleteAllByChat_Id(String chatId);
    @Transactional
    void deleteByUserAndChat(User user, Chat chat);
}
