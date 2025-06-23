package com.BaneseLabes.portal.service;

import com.BaneseLabes.portal.config.SecurityConfiguration;
import com.BaneseLabes.portal.dto.CreateUserDto;
import com.BaneseLabes.portal.dto.LoginUserDTO;
import com.BaneseLabes.portal.dto.RecoveryJwtTokenDto;
import com.BaneseLabes.portal.model.user.User;
import com.BaneseLabes.portal.model.user.Role;
import com.BaneseLabes.portal.model.user.UserDetailsImpl;
import com.BaneseLabes.portal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired private JwtTokenService jwtTokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    public RecoveryJwtTokenDto authenticateUser(LoginUserDTO loginUserDto) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserDto.email(), loginUserDto.password());

        Authentication authentication =
                authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return new RecoveryJwtTokenDto(jwtTokenService.generateToken(userDetails));
    }

    public void createUser(CreateUserDto createUserDto) {
        List<Role> role = Optional.ofNullable(createUserDto.role())
                .orElse(List.of())
                .stream()
                .map(roleName -> Role.builder()
                        .name(roleName)
                        .build())
                .toList();


        User newUser = User.builder()
                .email(createUserDto.email())
                .username(createUserDto.username())
                .companyName(createUserDto.companyName())
                .password(securityConfiguration.passwordEncoder().encode(createUserDto.password()))
                .cnpj(createUserDto.cnpj())
                .roles(role)
                .build();

        userRepository.save(newUser);
    }

    public Optional<User> updateUser(String email, String username, String password, String id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (email != null) {
                user.setEmail(email);
            }
            if (username != null) {
                user.setUsername(username);
            }
            if (password != null) {
                user.setPassword(password);
            }

            userRepository.save(user);
            return Optional.of(user);
        }
        return Optional.empty();
    }

    public Optional<User> getUser(String id) {
        return userRepository.findById(id);
    }

    public boolean deleteUser(String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }


}