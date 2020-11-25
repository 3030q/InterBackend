package ru.internaft.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.internaft.backend.entity.AccessData;
import ru.internaft.backend.entity.LoginData;
import ru.internaft.backend.entity.UserData;
import ru.internaft.backend.repository.AccessDataRepository;
import ru.internaft.backend.repository.LoginsDataRepository;
import ru.internaft.backend.repository.UsersDataRepository;

import java.util.Optional;

@RestController
public class AccessController {
    private final JsonNodeFactory jsonNodeFactory;
    private final AccessDataRepository accessDataRepository;
    private final LoginsDataRepository loginsDataRepository;
    private final UsersDataRepository usersDataRepository;

    public AccessController(AccessDataRepository accessDataRepository, LoginsDataRepository loginsDataRepository, UsersDataRepository usersDataRepository) {
        this.loginsDataRepository = loginsDataRepository;
        this.usersDataRepository = usersDataRepository;
        this.jsonNodeFactory = new ObjectMapper().getNodeFactory();
        this.accessDataRepository = accessDataRepository;
    }
    public Integer getCurrentUserId() {
        String loginCurrentUser = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return loginsDataRepository.findByLogin(loginCurrentUser).getUserData().getId();
    }

    @PostMapping("/adduser")
    public ResponseEntity<JsonNode> addingUsers(@RequestBody JsonNode requestJson){
        String email = requestJson.path("email").asText(null);
        String role = requestJson.path("role").asText(null);

        ObjectNode response = jsonNodeFactory.objectNode();
        Optional<AccessData> accessDataChecker =accessDataRepository.findById(getCurrentUserId());
        if (email == null || role == null) {
            response.put("status", "expected email, login and password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        AccessData accessDataRecord = accessDataRepository.findByEmail(email);

        if (accessDataRecord != null) {
            response.put("status", "email already in use");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        if(accessDataChecker.isPresent()) {
            if (accessDataChecker.get().getRole() != "ADMIN") {
                response.put("status", "access not allowed");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }
        AccessData accessData = new AccessData();
        accessData.setEmail(email);
        accessData.setRole(role);
        accessDataRepository.saveAndFlush(accessData);
        response.put("status", "creation was successful");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
