package com.example.task5.controller;

import com.example.task5.payload.req.RegisterDto;
import com.example.task5.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterDto registerDto) {
        authService.register(registerDto);
        return "redirect:/api/chats/chatPage";
    }
}
