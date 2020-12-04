package ru.internaft.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.internaft.backend.controller.UtilityController;
import ru.internaft.backend.entity.AccessData;
import ru.internaft.backend.entity.UserData;
import ru.internaft.backend.repository.AccessDataRepository;

import java.util.Optional;

@Service
public class AccessDataService {
    private final AccessDataRepository accessDataRepository;
    private final LoginDataService loginDataService;
    private final JsonNodeFactory jsonNodeFactory;
    private final UserDataService userDataService;

    public AccessDataService(AccessDataRepository accessDataRepository, LoginDataService loginDataService, UserDataService userDataService) {
        this.accessDataRepository = accessDataRepository;
        this.loginDataService = loginDataService;
        this.userDataService = userDataService;
        this.jsonNodeFactory = new ObjectMapper().getNodeFactory();
    }

    public AccessData findByEmail(String string) {
        return accessDataRepository.findByEmail(string);
    }

    public Optional<AccessData> findById(Integer id) {
        return accessDataRepository.findById(id);
    }

    public Integer deleteByEmail(String string) {
        return accessDataRepository.deleteByEmail(string);
    }

    public ResponseEntity<JsonNode> addUser(JsonNode requestJson) {
        UtilityController utilityController = new UtilityController(userDataService);
        String email = requestJson.path("email").asText(null);
        String role = requestJson.path("role").asText(null);
        ObjectNode response = jsonNodeFactory.objectNode();
        if (email == null || role == null) {
            response.put("status", "expected email, login and password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        UserData userData = userDataService.findById(utilityController.getCurrentUserId()).get();

        AccessData accessDataRecord = accessDataRepository.findByEmail(email);

        if (accessDataRecord != null) {
            response.put("status", "email already in use");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        if (!userData.getRole().equals("ADMIN")) {
            response.put("status", "access denied");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        AccessData accessData = new AccessData();
        accessData.setEmail(email);
        accessData.setRole(role);
        accessDataRepository.saveAndFlush(accessData);
        response.put("status", "creation was successful");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
