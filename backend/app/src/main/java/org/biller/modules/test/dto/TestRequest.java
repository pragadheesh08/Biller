
package org.biller.modules.test.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TestRequest(
    @NotBlank(message = "Name cannot be empty")
    @Size(max = 100, message = "Name is too long")
    String name,

    String message
) {}
