package com.BaneseLabes.portal.controller;

import com.BaneseLabes.portal.dto.CreateUserDto;
import com.BaneseLabes.portal.dto.LoginUserDTO;
import com.BaneseLabes.portal.dto.RecoveryJwtTokenDto;
import com.BaneseLabes.portal.dto.UpdateUserDTO;
import com.BaneseLabes.portal.model.user.User;
import com.BaneseLabes.portal.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.BaneseLabes.portal.config.JwtUtil;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private final JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    public UserController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<RecoveryJwtTokenDto> authenticateUser(
            @RequestBody LoginUserDTO loginUserDto) {
        RecoveryJwtTokenDto token = userService.authenticateUser(loginUserDto);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> createUser(@RequestBody CreateUserDto createUserDto) {
        userService.createUser(createUserDto);
        Map<String, String> response = Map.of("message", "Usuário criado com sucesso");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/authentication")
    public ResponseEntity<String> getAuthenticationTest() {
        return new ResponseEntity<>("Autenticado com sucesso", HttpStatus.OK);
    }

    @GetMapping("/authentication/customer")
    public ResponseEntity<String> getCustomerAuthenticationTest() {
        return new ResponseEntity<>("Cliente autenticado com sucesso", HttpStatus.OK);
    }

    @GetMapping("/authentication/admin")
    public ResponseEntity<String> getAdminAuthenticationTest() {
        return new ResponseEntity<>("Administrador autenticado com sucesso", HttpStatus.OK);
    }

    @GetMapping("/get")
    public Optional<User> getUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Claims claims = jwtUtil.extractClaims(token);
        String id = claims.get("id").toString();
        return userService.getUser(id);
    }

    @PatchMapping("/update")
    public ResponseEntity<User> updateUser(@RequestHeader("Authorization") String authHeader, @RequestBody UpdateUserDTO data) {
        String token = authHeader.replace("Bearer ", "");
        Claims claims = jwtUtil.extractClaims(token);
        String id = claims.get("id").toString();
        return  userService.updateUser(data.email(), data.username(), data.password(), id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<User> deleteUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Claims claims = jwtUtil.extractClaims(token);
        String id = claims.get("id").toString();
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
