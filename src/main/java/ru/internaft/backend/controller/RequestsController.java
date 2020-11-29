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
import ru.internaft.backend.entity.TokenData;
import ru.internaft.backend.entity.UserData;
import ru.internaft.backend.repository.LoginsDataRepository;
import ru.internaft.backend.repository.TokensDataRepository;
import ru.internaft.backend.repository.UsersDataRepository;

import java.util.Optional;


@RestController
public class RequestsController {
    private int idIncrement = 0;
    private final LoginsDataRepository loginsDataRepository;
    private final UsersDataRepository usersDataRepository;
    private final TokensDataRepository tokensDataRepository;
    private final JsonNodeFactory jsonNodeFactory;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public RequestsController(LoginsDataRepository loginsDataRepository,
                              UsersDataRepository usersDataRepository,
                              TokensDataRepository tokensDataRepository) {
        this.loginsDataRepository = loginsDataRepository;
        this.usersDataRepository = usersDataRepository;
        this.tokensDataRepository = tokensDataRepository;

        // TODO: try replace with beans
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
        this.jsonNodeFactory = new ObjectMapper().getNodeFactory();
    }


    @PostMapping("/user")
    public ResponseEntity<JsonNode> getUserData(@RequestBody JsonNode requestJson) {
        String token = requestJson.path("token").asText(null);
        Integer userId = requestJson.path("user_id").asInt(0);

        ObjectNode response = jsonNodeFactory.objectNode();

        if (token == null) {
            response.put("status", "expected token");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Optional<TokenData> tokenDataRecord = tokensDataRepository.findById(token);

        if (!tokenDataRecord.isPresent()) {
            response.put("status", "incorrect token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // TODO: add condition for proceed expired tokens (by timestamp)

        if (userId == 0) {
            userId = tokenDataRecord.get().getUserId();
        }

        UserData userData = usersDataRepository.findById(userId).get();
        // TODO: add case when not exists userdata, but exists userid

        // TODO: finalize code here!

        response.put("incorrect return data", "write me! 1234");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
