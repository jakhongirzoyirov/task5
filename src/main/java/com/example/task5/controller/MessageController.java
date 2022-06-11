package com.example.task5.controller;

import com.example.task5.payload.resp.ApiResponse;
import com.example.task5.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/byReceiverId/{receiverId}")
    public ResponseEntity<ApiResponse> getChatMessages(@PathVariable Long receiverId, @AuthenticationPrincipal UserDetails userDetails) {
        return messageService.getChatMessages(userDetails, receiverId);
    }
}
