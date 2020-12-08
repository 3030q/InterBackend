package ru.internaft.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.internaft.backend.controller.UtilityController;
import ru.internaft.backend.entity.MentorshipData;
import ru.internaft.backend.entity.UserData;
import ru.internaft.backend.repository.MentorshipDataRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MentorshipDataService {
    private final MentorshipDataRepository mentorshipDataRepository;
    private final JsonNodeFactory jsonNodeFactory;
    private final UserDataService userDataService;

    public MentorshipDataService(MentorshipDataRepository mentorshipDataRepository, UserDataService userDataService) {
        this.mentorshipDataRepository = mentorshipDataRepository;
        this.userDataService = userDataService;
        this.jsonNodeFactory = new ObjectMapper().getNodeFactory();
    }

    public ResponseEntity<JsonNode> makeLink(JsonNode requestJson) {
        ObjectNode response = jsonNodeFactory.objectNode();
        //defaultvalue = -1 потому что не нашел как null вставить, если найдешь варик зарефактори
        int interId = requestJson.path("intern").asInt(-1);
        int idCurrentUser = new UtilityController(userDataService).getCurrentUserId();
        UserData currentUser = userDataService.findById(idCurrentUser).get();
        if (!currentUser.getRole().equals("MENTOR")) {
            response.put("status", "access denied");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        MentorshipData mentorshipData = new MentorshipData();
        mentorshipData.setInternId(userDataService.findById(interId).get());
        mentorshipData.setMentorId(userDataService.findById(idCurrentUser).get());
        mentorshipDataRepository.saveAndFlush(mentorshipData);
        response.put("status", "alright");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Transactional
    public ResponseEntity<JsonNode> deleteLink(JsonNode requestJson) {
        ObjectNode responce = jsonNodeFactory.objectNode();
        Integer internId = requestJson.path("intern").asInt(-1);
        UserData currentUser = userDataService.findById(new UtilityController(userDataService).getCurrentUserId()).get();
        if (!currentUser.getRole().equals("ADMIN")) {
            responce.put("status", "access denied");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responce);
        }
        mentorshipDataRepository.deleteByInternId_Id(internId);
        responce.put("status", "alright");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responce);
    }

    public ResponseEntity<JsonNode> takeAllUsers(JsonNode requestJson) {
        ObjectNode response = jsonNodeFactory.objectNode();
        Integer userId = requestJson.path("user_id").asInt(-1);
        UserData currentUser = userDataService.findById(new UtilityController(userDataService).getCurrentUserId()).get();
        switch (currentUser.getRole()) {
            case "INTERN":
                int mentorId = mentorshipDataRepository.findByInternId_Id(currentUser.getId()).getMentorId().getId();
                response.put("links", mentorId);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            case "MENTOR": {
                List<MentorshipData> mentorshipData = mentorshipDataRepository.findAllByMentorId_Id(currentUser.getId());

            /*Здесь почему-то responce.putArray не принимает обычный массив,
            пришлось конвертнуть через objectMapper(взято со StackOverflow)
            в arrayNode
             */
                List<Integer> allLink = new ArrayList<Integer>();
                for (MentorshipData e : mentorshipData) {
                    allLink.add(e.getInternId().getId());
                }
                ArrayNode arrayNode = new ObjectMapper().valueToTree(allLink.toArray());
                response.putArray("interns").addAll(arrayNode);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }
            case "ADMIN": {
                List<MentorshipData> mentorshipData = mentorshipDataRepository.findAll();
                List<Integer> intern = new ArrayList<Integer>();
                Set<Integer> mentor = new HashSet<Integer>();
                for (MentorshipData e : mentorshipData) {
                    intern.add(e.getInternId().getId());
                    mentor.add(e.getMentorId().getId());
                }
                ArrayNode arrayNode1 = new ObjectMapper().valueToTree(intern.toArray());
                ArrayNode arrayNode2 = new ObjectMapper().valueToTree(mentor.toArray());
                response.putArray("interns").addAll(arrayNode1);
                response.putArray("mentors").addAll(arrayNode2);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }
            default:
                response.put("status", "bad_request");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<JsonNode> internsWithoutMentor() {
        UserData currentUser = userDataService.findById(new UtilityController(userDataService).getCurrentUserId()).get();
        ObjectNode response = jsonNodeFactory.objectNode();
        if (currentUser.getRole().equals("INTERN")) {
            response.put("status", "access denied");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        List<UserData> allIntern = userDataService.findAllByRole("INTERN");
        List<Integer> internsIdWithoutMentor = new ArrayList<>();
        for (UserData e : allIntern) {
            if (mentorshipDataRepository.findByInternId_Id(e.getId()) == null) {
                internsIdWithoutMentor.add(e.getId());
            }
        }
        ArrayNode arrayNode = new ObjectMapper().valueToTree(internsIdWithoutMentor);
        response.putArray("interns").addAll(arrayNode);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
