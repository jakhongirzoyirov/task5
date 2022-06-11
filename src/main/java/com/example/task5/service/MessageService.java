package com.example.task5.service;

import com.example.task5.entity.Chat;
import com.example.task5.entity.User;
import com.example.task5.payload.resp.ApiResponse;
import com.example.task5.repo.ChatRepository;
import com.example.task5.repo.MessageRepository;
import com.example.task5.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Objects;

import static com.example.task5.payload.resp.ApiResponse.response;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageService {

    private final UserRepo userRepo;
    private final ChatRepository chatRepo;
    private final MessageRepository messageRepo;

    public ResponseEntity<ApiResponse> getChatMessages(UserDetails userDetails,
                                                       Long receiverId) {
        Objects.requireNonNull(userDetails);
        Objects.requireNonNull(receiverId);

        User sender = (User) userDetails;
        User receiver = userRepo
                .findById(receiverId)
                .orElseThrow(() -> new IllegalStateException("ID: " + receiverId + " not found!"));

        Chat chat = chatRepo.findBySenderAndReceiver(sender, receiver).orElse(null);
        if (chat != null) {
            return response(messageRepo.getAllMessagesByChatId(chat.getChatId()));
        }
        return response(new ArrayList<>());
    }
}
