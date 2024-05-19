package com.just_talk.security_service.rest;

import com.just_talk.security_service.dto.AuthRequestDto;
import com.just_talk.security_service.dto.AuthResponseDto;
import com.just_talk.security_service.dto.UserDto;
import com.just_talk.security_service.entity.UserEntity;
import com.just_talk.security_service.mapper.UserMapper;
import com.just_talk.security_service.repository.UserRepository;
import com.just_talk.security_service.security.CustomPrincipal;
import com.just_talk.security_service.security.SecurityService;
import com.just_talk.security_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthRestControllerV1 {
    private final SecurityService securityService;
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public Mono<UserDto> register(@RequestBody UserDto dto){
        UserEntity entity = userMapper.map(dto);
        return userService.registerUser(entity)
                .map(userMapper::map);
    }

    @PostMapping("/login")
    public Mono<AuthResponseDto> login (@RequestBody AuthRequestDto dto){
        return securityService.authenticate(dto.getUsername(), dto.getPassword())
                .flatMap(tokenDetails->Mono.just(
                        AuthResponseDto.builder()
                                .userId(tokenDetails.getUserId())
                                .token(tokenDetails.getToken())
                                .issuedAt(tokenDetails.getIssuedAt())
                                .expiresAt(tokenDetails.getExpiresAt())
                                .build()
                ));
    }

    @GetMapping("/info")
    public Mono<UserDto> getUserInfo(Authentication authentication){
        CustomPrincipal customPrincipal = (CustomPrincipal) authentication.getPrincipal();

        return userService.getUserById(customPrincipal.getId())
                .map(userMapper::map);
    }
}
