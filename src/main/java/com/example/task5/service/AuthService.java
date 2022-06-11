package com.example.task5.service;

import com.example.task5.entity.User;
import com.example.task5.payload.req.RegisterDto;
import com.example.task5.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserRepo userRepo;

    public void register(RegisterDto registerDto) {
        User user = userRepo.findByName(registerDto.getName()).orElse(null);
        if (user == null) {
            user = userRepo.save(new User(registerDto.getName()));
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
