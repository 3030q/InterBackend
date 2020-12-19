package ru.internaft.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
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

    @PostMapping("/upload-result/{taskId}")
    public ResponseEntity<JsonNode> uploadResult(@PathVariable int taskId, MultipartFile file) throws IOException {
        return fileDataService.uploadResult(file,taskId);
    }

    @GetMapping(value = "/download-file/{fileId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody
    byte[] getDocument(@PathVariable int fileId) throws IOException {
        return fileDataService.getFile(fileId);
    }

    @GetMapping("/remove-document/{fileId}")
    public ResponseEntity<JsonNode> removeDocument(@PathVariable int fileId) throws IOException {
        return fileDataService.removeDocument(fileId);
    }

    @GetMapping("/remove-result/{fileId}")
    public ResponseEntity<JsonNode> removeResult(@PathVariable int fileId) throws IOException {
        return fileDataService.removeResult(fileId);
    }
}
