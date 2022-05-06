package me.toyproject.loginjwt.controller;

import me.toyproject.loginjwt.dto.UserDto;
import me.toyproject.loginjwt.entity.User;
import me.toyproject.loginjwt.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 회원 가입
 */
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 회원 가입 API
     *
     * @param userDto
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity<User> signup(@Valid @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.signup(userDto));
    }

    /**
     * 사용자의 유저 정보 및 인증 정보를 가져온다.
     *
     * @return
     */
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole ('USER', 'ADMIN')") // 일반 유저, 관리자 모두 권한을 호출 가능하게 설정.
    public ResponseEntity<User> getMyUserInfo() {
        return ResponseEntity.ok(userService.getMyUserWithAuthorities().get());
    }


    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole ('ADMIN')")//관리자만 사용자 정보를 가져올 수 있도록 설정
    public ResponseEntity<User> getUserInfo(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserWithAuthorities(username).get());
    }
}