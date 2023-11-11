package fpt.capstone.buildingmanagementsystem.repository;

import fpt.capstone.buildingmanagementsystem.model.entity.ChatMessage;
import fpt.capstone.buildingmanagementsystem.model.entity.OvertimeLog;
import fpt.capstone.buildingmanagementsystem.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    @Query(value = "select * from chat_message where (sender_id = :from and receiver_id = :to) or (sender_id = :to and receiver_id = :from) order by create_at ASC", nativeQuery = true)
    List<ChatMessage> findBySenderIdAndReceiverId(String from, String to);
    List<ChatMessage> findAllByReceiver(User user);

    List<ChatMessage> findAllBySender(User user);

}
