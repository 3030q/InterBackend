package ru.internaft.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.internaft.backend.service.AccessDataService;


@RestController
public class AccessController {
    private final AccessDataService accessDataService;

    public AccessController(AccessDataService accessDataRepository) {
        this.accessDataService = accessDataRepository;
    }

    @PostMapping("/adduser")
    public ResponseEntity<JsonNode> addingUser(@RequestBody JsonNode requestJson) {
        return accessDataService.addUser(requestJson);
    }
}
