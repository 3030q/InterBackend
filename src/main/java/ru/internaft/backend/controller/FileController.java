package ru.internaft.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<JsonNode> uploadAvatar(@RequestParam("file") MultipartFile file) throws IOException {
        return fileDataService.uploadAvatar(file);
    }

    @PostMapping("/upload-document/{taskId}")
    public ResponseEntity<JsonNode> uploadDocument(@PathVariable int taskId, MultipartFile file) throws IOException {
        return fileDataService.uploadDocument(file,taskId);
    }

    @GetMapping(value = "/download-document/{fileId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody
    byte[] getImage(@PathVariable int fileId) throws IOException {
        return fileDataService.getFile(fileId);
    }
}
