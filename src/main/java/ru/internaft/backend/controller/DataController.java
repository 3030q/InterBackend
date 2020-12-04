package ru.internaft.backend.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.internaft.backend.service.FileDataService;
import ru.internaft.backend.service.UserDataService;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;

@RestController
public class DataController {
    private final UserDataService userDataService;
    private final JsonNodeFactory jsonNodeFactory;
    private final ServletContext servletContext;
    private final FileDataService fileDataService;

    public DataController(UserDataService userDataService, ServletContext servletContext, FileDataService fileDataService) {
        this.userDataService = userDataService;
        this.servletContext = servletContext;
        this.fileDataService = fileDataService;
        this.jsonNodeFactory = new ObjectMapper().getNodeFactory();
    }

    @GetMapping("/user")
    public ResponseEntity<JsonNode> takeCurrentUserData() {
        return userDataService.takeCurrentUserData(userDataService);
    }

    @GetMapping(value = "/avatarimage", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getImage(@RequestBody JsonNode requestJson) throws IOException {
        return fileDataService.getImage(requestJson);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<JsonNode> takeUserData(@PathVariable int userId) {
        return userDataService.takeUserData(userDataService, userId);
    }

}
