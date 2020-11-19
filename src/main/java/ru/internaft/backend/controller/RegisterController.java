package ru.internaft.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.internaft.backend.entity.LoginData;
import ru.internaft.backend.repository.LoginsDataRepository;
import ru.internaft.backend.repository.TokensDataRepository;
import ru.internaft.backend.repository.UsersDataRepository;

import java.util.Optional;

@RestController
public class RegisterController {
    private final LoginsDataRepository loginsDataRepository;
    private final UsersDataRepository usersDataRepository;
    private final TokensDataRepository tokensDataRepository;
    private final JsonNodeFactory jsonNodeFactory;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public RegisterController(LoginsDataRepository loginsDataRepository,
                              UsersDataRepository usersDataRepository,
                              TokensDataRepository tokensDataRepository) {
        this.loginsDataRepository = loginsDataRepository;
        this.usersDataRepository = usersDataRepository;
        this.tokensDataRepository = tokensDataRepository;

        // TODO: try replace with beans
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
        this.jsonNodeFactory = new ObjectMapper().getNodeFactory();
    }

    @PostMapping("/sign-up")
    public ResponseEntity<JsonNode> signUp(@RequestBody JsonNode requestJson) {
        String login = requestJson.path("email").asText(null);
        String password = requestJson.path("password").asText(null);
        String fullName = requestJson.path("name").asText(null);

        ObjectNode response = jsonNodeFactory.objectNode();

        if (login == null || password == null || fullName == null) {
            response.put("status", "expected email, login and password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Optional<LoginData> loginDataRecord = loginsDataRepository.findById(login);

        if (loginDataRecord.isPresent()) {
            response.put("status", "email already in use");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        LoginData newUser = new LoginData();
        newUser.setLogin(login);
        newUser.setPassword((bCryptPasswordEncoder.encode(password)));
        newUser.setId(123);
        loginsDataRepository.saveAndFlush(newUser);
        response.put("token", "123");

        // TODO: generate token
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
