package fpt.capstone.buildingmanagementsystem.repository;

import fpt.capstone.buildingmanagementsystem.model.entity.Chat;
import fpt.capstone.buildingmanagementsystem.model.entity.UnReadChat;
import fpt.capstone.buildingmanagementsystem.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnReadChatRepository extends JpaRepository<UnReadChat, String> {
    boolean existsUnReadChatByChatAndUser(Chat chat, User user);
}
