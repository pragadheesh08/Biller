package org.biller.modules.user.controller;

import java.sql.SQLException;

import org.biller.modules.user.dto.RegisterRequest;
import org.biller.modules.user.dto.RegisterResponse;
import org.biller.modules.user.model.User;
import org.biller.modules.user.repository.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class RegisterController {
    private final UserRepository repository;

    public RegisterController(UserRepository repository){
        this.repository = repository;
    }

    @PostMapping("/register")
    public RegisterResponse registerUser(@Valid @RequestBody RegisterRequest request) {
        
        User user;
        try {
            user = repository.create(
                request.username(),
                request.email(),
                request.password()
            );
            return new RegisterResponse("success", user.id());
        } catch (SQLException e) {
            e.printStackTrace();
            return new RegisterResponse("failed", 0);
        }
    }
}