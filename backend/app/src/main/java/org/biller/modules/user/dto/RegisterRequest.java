package org.biller.modules.user.dto;

public record RegisterRequest(
    String username,
    String email,
    String password
) {}