package ru.internaft.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.internaft.backend.repository.AccessDataRepository;
import ru.internaft.backend.repository.LoginsDataRepository;
import ru.internaft.backend.repository.TokensDataRepository;
import ru.internaft.backend.repository.UsersDataRepository;
import ru.internaft.backend.service.RegisterService;

@RestController
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<JsonNode> signUp(@RequestBody JsonNode requestJson) {
       return registerService.register(requestJson);
    }
}
