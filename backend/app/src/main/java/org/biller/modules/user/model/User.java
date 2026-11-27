package org.biller.modules.user.model;

import java.time.Instant;

public record User(
    Long id,
    String username,
    String email,
    String passwordHash,
    Instant createdAt
) {}
