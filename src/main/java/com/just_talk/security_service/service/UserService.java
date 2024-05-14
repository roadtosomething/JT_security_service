package com.just_talk.security_service.service;

import com.just_talk.security_service.entity.UserEntity;
import com.just_talk.security_service.entity.UserRole;
import com.just_talk.security_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Mono<UserEntity> registerUser(UserEntity user){
         return userRepository.save(
                user.toBuilder()
                        .password(passwordEncoder.encode(user.getPassword()))
                        .role(UserRole.EXPERT)
                        .enabled(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        ).doOnSuccess(u ->{
                     log.info("IN registerUser - user: {} created", u);
                 });
    }

    public Mono<UserEntity> getUserById(Long id){
        return userRepository.findById(id);
    }

    public Mono<UserEntity> getUserbyUsername (String username){
        return userRepository.findByUsername(username);
    }
}
