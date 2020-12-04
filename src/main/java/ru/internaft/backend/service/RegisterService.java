package ru.internaft.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.internaft.backend.entity.LoginData;
import ru.internaft.backend.entity.UserData;
import ru.internaft.backend.repository.LoginsDataRepository;
import ru.internaft.backend.repository.UsersDataRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class RegisterService {
    private final JsonNodeFactory jsonNodeFactory;
    private final LoginDataService loginDataService;
    private final AccessDataService accessDataService;
    private final UsersDataRepository usersDataRepository;
    private final LoginsDataRepository loginsDataRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public RegisterService(LoginDataService loginDataService, AccessDataService accessDataService, UsersDataRepository usersDataRepository, LoginsDataRepository loginsDataRepository) {
        this.loginDataService = loginDataService;
        this.accessDataService = accessDataService;
        this.usersDataRepository = usersDataRepository;
        this.loginsDataRepository = loginsDataRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
        this.jsonNodeFactory = new ObjectMapper().getNodeFactory();
    }

    @Transactional
    public ResponseEntity<JsonNode> register(JsonNode requestJson) {
        String login = requestJson.path("email").asText(null);
        String password = requestJson.path("password").asText(null);
        String fullName = requestJson.path("name").asText(null);
        ObjectNode response = jsonNodeFactory.objectNode();
        if (login == null || password == null || fullName == null) {
            response.put("status", "expected email, login and password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Optional<LoginData> loginDataRecord = loginDataService.findById(login);

        if (loginDataRecord.isPresent()) {
            response.put("status", "email already in use");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        if (accessDataService.findByEmail(login) == null) {
            response.put("status", "access not allowed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        UserData newUserData = new UserData();
        //TODO: разобраться с добавлением и управлением ролями
        newUserData.setRole(accessDataService.findByEmail(login).getRole());
        newUserData.setEmail(login);
        newUserData.setFullName(fullName);
        usersDataRepository.saveAndFlush(newUserData);

        accessDataService.deleteByEmail(login);

        LoginData newLoginData = new LoginData();
        newLoginData.setLogin(login);
        newLoginData.setPassword((bCryptPasswordEncoder.encode(password)));
        newLoginData.setUserData(usersDataRepository.findByEmail(login));
        loginsDataRepository.saveAndFlush(newLoginData);
        response.put("status", "alright");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
