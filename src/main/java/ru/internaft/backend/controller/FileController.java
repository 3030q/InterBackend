package ru.internaft.backend.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.internaft.backend.service.FileDataService;

import java.io.IOException;

@RestController
public class FileController {
    private final FileDataService fileDataService;

    public FileController(FileDataService fileDataService) {
        this.fileDataService = fileDataService;
    }

    @PostMapping("/uploadavatar")
    public ResponseEntity<ObjectNode> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return fileDataService.uploadFile(file);
    }
}
