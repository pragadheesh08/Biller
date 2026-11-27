package org.biller.modules.test.controller;

import jakarta.validation.Valid;

import org.biller.modules.test.dto.TestRequest;
import org.biller.modules.test.dto.TestResponse;
import org.biller.modules.test.service.TestService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private final TestService service;

    public TestController(TestService service) {
        this.service = service;
    }

    @GetMapping
    public TestResponse getInfo() {
        return new TestResponse(
            "success",
            "GET request successful",
            service.getInfo(),
            Instant.now()
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TestResponse create(
            @Valid @RequestBody TestRequest request) {

        return new TestResponse(
            "success",
            "POST request successful",
            service.process(request),
            Instant.now()
        );
    }

    @PutMapping("/{id}")
    public TestResponse update(
            @PathVariable String id,
            @RequestBody Map<String, Object> body) {

        return new TestResponse(
            "success",
            "PUT request successful",
            Map.of("id", id, "received", body),
            Instant.now()
        );
    }

    @DeleteMapping("/{id}")
    public TestResponse delete(@PathVariable String id) {
        return new TestResponse(
            "success",
            "DELETE request successful",
            Map.of("deletedId", id),
            Instant.now()
        );
    }
}
