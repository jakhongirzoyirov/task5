package com.example.task5.service;

import com.example.task5.entity.User;
import com.example.task5.payload.req.SearchUserDto;
import com.example.task5.payload.resp.ApiResponse;
import com.example.task5.projection.UserProjection;
import com.example.task5.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.example.task5.payload.resp.ApiResponse.response;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo
                .findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username: " + username + " not found!"));
    }

    public ResponseEntity<ApiResponse> getMe(UserDetails userDetails) {
        Objects.requireNonNull(userDetails);
        User currentUser = (User) userDetails;
        return response(userRepo.getUserProjectionById(currentUser.getId()));
    }

    public ResponseEntity<ApiResponse> getUsers() {
        return response(userRepo.getAllUsers());
    }

    public ResponseEntity<List<UserProjection>> searchUsers(SearchUserDto searchUserDto) {
        return ResponseEntity.ok(userRepo.searchUsers(searchUserDto.getSearchText()));
    }
}
