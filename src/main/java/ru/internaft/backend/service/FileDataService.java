package ru.internaft.backend.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.internaft.backend.controller.UtilityController;
import ru.internaft.backend.entity.FileData;
import ru.internaft.backend.entity.UserData;
import ru.internaft.backend.repository.FileDataRepository;
import ru.internaft.backend.repository.UsersDataRepository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileDataService {
    private final UserDataService userDataService;
    private final FileDataRepository fileDataRepository;
    private final UsersDataRepository usersDataRepository;
    private final JsonNodeFactory jsonNodeFactory;

    @Value("${app.upload.dir}")
    public String uploadDir;

    public FileDataService(UserDataService userDataService, FileDataRepository fileDataRepository, UsersDataRepository usersDataRepository) {
        this.userDataService = userDataService;
        this.fileDataRepository = fileDataRepository;
        this.usersDataRepository = usersDataRepository;
        this.jsonNodeFactory = new ObjectMapper().getNodeFactory();
    }

    public ResponseEntity<ObjectNode> uploadFile(MultipartFile file) throws IOException {
        UtilityController utilityController = new UtilityController(userDataService);
        int id = utilityController.getCurrentUserId();
        String path = uploadDir + File.separator + id
                + StringUtils.cleanPath(file.getOriginalFilename());
        //Так как spring не видит вне resource добавим короткий путь и будем его класть в бд
        //TODO:обсудить это
        String shortPath = id + StringUtils.cleanPath(file.getOriginalFilename());
        Path copyLocation = Paths.get(path);
        try {
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //TODO: подумать как лучше айдишник вытаскивать
        //Да! ГОвНо код и что ты мне сделаешь?
        UserData userData = userDataService.findById(id).get();
        if (userData.getAvatarData() != null) {
            FileData fileDataActualUser = fileDataRepository.findById(userData.getAvatarData().getId()).get();
            Files.deleteIfExists(Paths.get(uploadDir + File.separator + fileDataActualUser.getPath()));
            fileDataActualUser.setPath(shortPath);

            fileDataRepository.saveAndFlush(fileDataActualUser);
        } else {
            FileData fileData = new FileData();
            fileData.setPath(shortPath);
            fileDataRepository.saveAndFlush(fileData);
            userData.setAvatarData((fileDataRepository.findByPath(shortPath)));
            usersDataRepository.saveAndFlush(userData);
        }
        ObjectNode response = jsonNodeFactory.objectNode();
        response.put("status", "alright");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public byte[] getImage(JsonNode requestJson) throws IOException {
        int user_id = requestJson.path("user_id").asInt(-1);
        InputStream in = getClass().getResourceAsStream("/file/" + userDataService
                .findById(user_id)
                .get().getAvatarData().getPath());
        return IOUtils.toByteArray(in);
    }

}
