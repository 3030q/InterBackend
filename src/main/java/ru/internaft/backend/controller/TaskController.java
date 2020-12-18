package ru.internaft.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.internaft.backend.service.TaskAndReviewDataService;

import java.text.ParseException;

@RestController
public class TaskController {
    private final TaskAndReviewDataService taskAndReviewDataService;

    public TaskController(TaskAndReviewDataService taskAndReviewDataService) {
        this.taskAndReviewDataService = taskAndReviewDataService;
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<JsonNode> takeTask(@PathVariable int taskId) {
        return taskAndReviewDataService.takeTask(taskId);
    }

    @PostMapping("/add-task")
    public ResponseEntity<JsonNode> addTask(@RequestBody JsonNode requestJson) throws ParseException {
        return taskAndReviewDataService.addTask(requestJson);
    }

    @PostMapping("/remove-task/{taskId}")
    public ResponseEntity<JsonNode> removeTask(@PathVariable int taskId) {
        return taskAndReviewDataService.removeTask(taskId);
    }

    @GetMapping("/tasks/{userId}")
    public ResponseEntity<JsonNode> takeAllInternTasks(@PathVariable int userId) {
        return taskAndReviewDataService.takeAllUserTasks(userId);
    }

    @PostMapping("/update-task")
    public ResponseEntity<JsonNode> updateTask(@RequestBody JsonNode requestJson) {
        return taskAndReviewDataService.updateTask(requestJson);
    }

    @PostMapping("/send-in-check/{taskId}")
    public ResponseEntity<JsonNode> sendInCheck(@PathVariable int taskId) {
        return taskAndReviewDataService.sendInCheck(taskId);
    }

    @PostMapping("/send-to-rework/{taskId}")
    public ResponseEntity<JsonNode> sendToRework(@PathVariable int taskId) {
        return taskAndReviewDataService.sendToRework(taskId);
    }

    @PostMapping("/complete-the-task/{taskId}")
    public ResponseEntity<JsonNode> completeTheTask(@PathVariable int taskId, @RequestBody JsonNode requestJson) {
        return taskAndReviewDataService.completeTheTask(taskId,requestJson);
    }

    @GetMapping("/reviews/{userId}")
    public ResponseEntity<JsonNode> allReviewsByUser(@PathVariable int userId){
        return taskAndReviewDataService.allReviewsByUser(userId);
    }
}
