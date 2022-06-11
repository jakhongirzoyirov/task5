package com.example.task5.repo;

import com.example.task5.entity.Chat;
import com.example.task5.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    boolean existsBySenderIdAndReceiverId(Long sender_id, Long receiver_id);

    Chat getBySenderIdAndReceiverId(Long sender_id, Long receiver_id);

    Optional<Chat> findBySenderAndReceiver(User sender, User receiver);

    boolean existsBySenderAndReceiver(User sender, User receiver);
}
