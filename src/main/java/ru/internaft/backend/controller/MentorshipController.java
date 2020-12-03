package ru.internaft.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.internaft.backend.service.MentorshipDataService;

@RestController
public class MentorshipController {
    private final MentorshipDataService mentorshipDataService;

    public MentorshipController(MentorshipDataService mentorshipDataService) {
        this.mentorshipDataService = mentorshipDataService;
    }

    @PostMapping("/makelink")
    public ResponseEntity<JsonNode> makeLink(@RequestBody JsonNode requestJson){
        return mentorshipDataService.makeLink(requestJson);
    }

    @PostMapping("/deletelink")
    public ResponseEntity<JsonNode> deleteLink(@RequestBody JsonNode requestJson){
        return mentorshipDataService.deleteLink(requestJson);
    }
    @PostMapping("/users")
    public ResponseEntity<JsonNode> takeAllLink(@RequestBody JsonNode requestJson){
        return mentorshipDataService.takeAllUsers(requestJson);
    }

    @GetMapping("/withoutmentor")
    public ResponseEntity<JsonNode> intersWithoutMentor(){
        return mentorshipDataService.internsWithoutMentor();
    }
}
