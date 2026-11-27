package org.biller.modules.user.controller;

import org.biller.modules.user.dto.RegisterRequest;
import org.biller.modules.user.dto.RegisterResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class RegisterController {
    @PostMapping("/register")
    public RegisterResponse registerUser(@Valid @RequestBody RegisterRequest request) {

        int generatedId = 1; // This should be replaced with actual user ID generation logic
        System.out.println("User registered with ID: " + generatedId);
        return new RegisterResponse("success", generatedId);
    }
}