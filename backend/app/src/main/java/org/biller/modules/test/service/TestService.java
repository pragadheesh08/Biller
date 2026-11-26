package org.biller.modules.test.service;

import org.biller.modules.dto.TestRequest;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class TestService {

    public Map<String, Object> getInfo() {
        return Map.of(
            "framework", "Spring Boot",
            "language", "Java",
            "status", "Operational"
        );
    }

    public Map<String, Object> process(TestRequest request) {
        return Map.of(
            "id", UUID.randomUUID().toString(),
            "name", request.name(),
            "message", request.message() == null
                ? "" : request.message()
        );
    }
}
