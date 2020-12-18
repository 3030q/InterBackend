package ru.internaft.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.internaft.backend.controller.UtilityController;
import ru.internaft.backend.entity.UserData;
import ru.internaft.backend.repository.UsersDataRepository;

import java.util.List;
import java.util.Optional;


@Service
public class UserDataService {
    private final JsonNodeFactory jsonNodeFactory;
    private final UsersDataRepository usersDataRepository;

    public UserDataService(UsersDataRepository usersDataRepository) {
        this.usersDataRepository = usersDataRepository;
        this.jsonNodeFactory = new ObjectMapper().getNodeFactory();
    }

    public UserData findByEmail(String string) {
        return usersDataRepository.findByEmail(string);
    }

    public Optional<UserData> findById(Integer id) {
        return usersDataRepository.findById(id);
    }

    public List<UserData> findAllByRole(String role) {
        return usersDataRepository.findAllByRole(role);
    }


    public ResponseEntity<JsonNode> takeCurrentUserData(UserDataService userDataService) {
        UtilityController utilityController = new UtilityController(userDataService);
        UserData userData = utilityController.getCurrentUser();
        ObjectNode response = jsonNodeFactory.objectNode();
        response.put("userId", userData.getId());
        response.put("email", userData.getEmail());
        response.put("name", userData.getFullName());
        response.put("about", userData.getAboutText());
        response.put("role", userData.getRole());
        //тут проверка на null, иначе все сломается
        if (userData.getAvatarData() != null) {
            response.put("avatar_id", userData.getAvatarData().getId());
        } else {
            response.put("avatar_id", (Integer) null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<JsonNode> takeUserData(UserDataService userDataService, Integer userId) {
        ObjectNode response = jsonNodeFactory.objectNode();
        if (!userDataService.findById(userId).isPresent()) {
            response.put("status", "user does not exist");
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        UserData userData = userDataService.findById(userId).get();
        response.put("userId", userData.getId());
        response.put("email", userData.getEmail());
        response.put("name", userData.getFullName());
        response.put("about", userData.getAboutText());
        response.put("role", userData.getRole());
        if (userData.getAvatarData() != null) {
            response.put("avatar_id", userData.getAvatarData().getId());
        } else {
            response.put("avatar_id", (Integer) null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<JsonNode> deleteUser(int userId, UtilityController utilityController){
        ObjectNode response = jsonNodeFactory.objectNode();
        UserData currentUser = utilityController.getCurrentUser();
        if(currentUser.getRole().equals("ADMIN")){
            Optional<UserData> userForDelete = usersDataRepository.findById(userId);
            if(userForDelete.isPresent()){
                try{
                    usersDataRepository.deleteById(userId);
                    response.put("status","alright");
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
                }catch (Exception e){
                    response.put("status", String.valueOf(e));
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }
            }else{
                response.put("status", "user for delete is not present");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }else{
            response.put("status", "access denied");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
