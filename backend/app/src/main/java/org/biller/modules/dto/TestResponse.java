package org.biller.modules.dto;

import java.time.Instant;

public record TestResponse(
    String status,
    String message,
    Object data,
    Instant timestamp
) {}
