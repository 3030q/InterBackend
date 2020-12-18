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
import ru.internaft.backend.entity.TaskData;
import ru.internaft.backend.entity.UserData;
import ru.internaft.backend.repository.FileDataRepository;
import ru.internaft.backend.repository.TasksDataRepository;
import ru.internaft.backend.repository.UsersDataRepository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;

@Service
public class FileDataService {
    private final UserDataService userDataService;
    private final FileDataRepository fileDataRepository;
    private final UsersDataRepository usersDataRepository;
    private final JsonNodeFactory jsonNodeFactory;
    private final UtilityController utilityController;
    private final TasksDataRepository tasksDataRepository;

    @Value("${app.upload.dir}")
    public String uploadDir;

    public FileDataService(UserDataService userDataService, FileDataRepository fileDataRepository, UsersDataRepository usersDataRepository, UtilityController utilityController, TasksDataRepository tasksDataRepository) {
        this.userDataService = userDataService;
        this.fileDataRepository = fileDataRepository;
        this.usersDataRepository = usersDataRepository;
        this.utilityController = utilityController;
        this.tasksDataRepository = tasksDataRepository;
        this.jsonNodeFactory = new ObjectMapper().getNodeFactory();
    }

    public ResponseEntity<JsonNode> uploadDocument(MultipartFile file, int taskId) throws IOException {
        ObjectNode response = jsonNodeFactory.objectNode();
        Optional<TaskData> taskData = tasksDataRepository.findById(taskId);
        if(!taskData.isPresent()){
            response.put("status", "task is not present");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        TaskData task = taskData.get();
        int id = utilityController.getCurrentUserId();
        if(task.getMentorId().getId() != id){
            response.put("status", "access denied");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        String path = uploadDir + File.separator + id
                + StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
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
        //Да! ГОвНо код и что ты мне сделаешь
        FileData fileData = new FileData();
        fileData.setPath(shortPath);
        fileData.setFileName(file.getOriginalFilename());
        fileData.setTask(task);
        fileDataRepository.saveAndFlush(fileData);
        response.put("status", "alright");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<JsonNode> uploadAvatar(MultipartFile file) throws IOException {
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
        UserData userData =  utilityController.getCurrentUser();
        if (userData.getAvatarData() != null) {
            FileData fileDataActualUser = fileDataRepository.findById(userData.getAvatarData().getId()).get();
            Files.deleteIfExists(Paths.get(uploadDir + File.separator + fileDataActualUser.getPath()));
            fileDataActualUser.setPath(shortPath);
            fileDataActualUser.setFileName(file.getOriginalFilename());
            fileDataRepository.saveAndFlush(fileDataActualUser);
        } else {
            FileData fileData = new FileData();
            fileData.setPath(shortPath);
            fileData.setFileName(file.getOriginalFilename());
            fileDataRepository.saveAndFlush(fileData);
            userData.setAvatarData((fileDataRepository.findByPath(shortPath)));
            usersDataRepository.saveAndFlush(userData);
        }
        ObjectNode response = jsonNodeFactory.objectNode();
        response.put("status", "alright");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public byte[] getAvatar(int userId) throws IOException {
        if(!usersDataRepository.findById(userId).isPresent()){
            return null;
        }
        InputStream in = getClass().getResourceAsStream("/file/" + usersDataRepository
                .findById(userId)
                .get().getAvatarData().getPath());
        return IOUtils.toByteArray(in);
    }

    public byte[] getFile(int fileId) throws IOException {
        Optional<FileData> fileData = fileDataRepository.findById(fileId);
        if(!fileData.isPresent()){
            return null;
        }
        InputStream in = getClass().getResourceAsStream("/file/" + fileData.get().getPath());
        return IOUtils.toByteArray(in);
    }
}
