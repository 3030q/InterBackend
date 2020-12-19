package ru.internaft.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.internaft.backend.SpringAppCache.ReturnedDocument;
import ru.internaft.backend.SpringAppCache.ReturnedScore;
import ru.internaft.backend.controller.UtilityController;
import ru.internaft.backend.entity.*;
import ru.internaft.backend.repository.FileDataRepository;
import ru.internaft.backend.repository.ReviewsDataRepository;
import ru.internaft.backend.repository.TasksDataRepository;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskAndReviewDataService {
    private final ReviewsDataRepository reviewsDataRepository;
    private final TasksDataRepository tasksDataRepository;
    private final JsonNodeFactory jsonNodeFactory;
    private final UtilityController utilityController;
    private final UserDataService userDataService;
    private final FileDataRepository fileDataRepository;

    public TaskAndReviewDataService(ReviewsDataRepository reviewsDataRepository,
                                    TasksDataRepository tasksDataRepository,
                                    UtilityController utilityController,
                                    UserDataService userDataService, FileDataRepository fileDataRepository) {
        this.reviewsDataRepository = reviewsDataRepository;
        this.tasksDataRepository = tasksDataRepository;
        this.utilityController = utilityController;
        this.userDataService = userDataService;
        this.fileDataRepository = fileDataRepository;
        this.jsonNodeFactory = new ObjectMapper().getNodeFactory();
    }

    public ResponseEntity<JsonNode> addTask(JsonNode requestJson) {
        UserData currentUser = utilityController.getCurrentUser();
        ObjectNode response = jsonNodeFactory.objectNode();
        if (!currentUser.getRole().equals("MENTOR")) {
            response.put("status", "access denied");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        Optional<UserData> intern = userDataService.findById(requestJson.path("intern_id").asInt(-1));
        if (!intern.isPresent()) {
            response.put("status", "intern is not present");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        if (!intern.get().getRole().equals("INTERN")) {
            response.put("status", "this user is not intern");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        String deadlineDate = requestJson.path("deadline_date").asText(null);
        String taskDescription = requestJson.path("task_description").asText(null);
        TaskData task = new TaskData();
        task.setInternId(intern.get());
        //Кладем актуальное время на сервере
        task.setCreationDate(new Timestamp(System.currentTimeMillis()));
        task.setDeadlineDate(Timestamp.valueOf(deadlineDate));
        task.setStatus("IN_WORK");
        task.setTaskDescription(taskDescription);
        task.setMentorId(currentUser);
        tasksDataRepository.saveAndFlush(task);
        response.put("status", "alright");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<JsonNode> takeTask(Integer taskId) {
        ObjectNode response = jsonNodeFactory.objectNode();
        Optional<TaskData> task = tasksDataRepository.findById(taskId);
        if (task.isPresent()) {
            TaskData presentTask = task.get();
            response.put("task_id", presentTask.getId());
            response.put("intern_id", presentTask.getInternId().getId());
            response.put("creation_date", presentTask.getCreationDate().toString());
            response.put("deadline_date", presentTask.getDeadlineDate().toString());
            response.put("status", presentTask.getStatus());
            response.put("task_description", presentTask.getTaskDescription());
            response.put("mentor_id", presentTask.getMentorId().getId());
            List<FileData> fileForTask = fileDataRepository.findAllByTask_Id(presentTask.getId());
            List<ReturnedDocument> result = new ArrayList<>();
            List<ReturnedDocument> documents = new ArrayList<>();
            for (FileData fileData : fileForTask) {
                if (fileData.getType().equals("DOCUMENT")) {
                    documents.add(new ReturnedDocument(fileData.getFileName(), fileData.getId()));
                } else if (fileData.getType().equals("RESULT")) {
                    result.add(new ReturnedDocument(fileData.getFileName(), fileData.getId()));
                }
            }
            ArrayNode arrayNode = new ObjectMapper().valueToTree(result);
            ArrayNode arrayNode2 = new ObjectMapper().valueToTree(documents);
            response.putArray("result").addAll(arrayNode);
            response.putArray("document").addAll(arrayNode2);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        } else {
            response.put("status", "task is not present");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<JsonNode> updateTask(JsonNode requestJson) {
        ObjectNode response = jsonNodeFactory.objectNode();
        int currentUserId = utilityController.getCurrentUserId();
        UserData currentUser = utilityController.getCurrentUser();
        if (currentUser.getRole().equals("MENTOR")) {
            response.put("status", "access denied");
        }
        Integer taskId = requestJson.path("task_id").asInt(-1);
        Optional<TaskData> taskData = tasksDataRepository.findById(taskId);
        if (!taskData.isPresent()) {
            response.put("status", "task is not present");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        TaskData task = taskData.get();
        if (task.getMentorId().getId() != currentUserId) {
            response.put("status", "you can't update this task");
        }
        String taskDescription = requestJson.path("task_description").asText(null);
        String deadlineDate = requestJson.path("deadline_date").asText(null);
        //TODO:зарефакторить ветку if-ов
        if (taskDescription != null || deadlineDate != null) {
            if (taskDescription != null) {
                task.setTaskDescription(taskDescription);
            }
            if (deadlineDate != null) {
                task.setDeadlineDate(Timestamp.valueOf(deadlineDate));
            }
            tasksDataRepository.saveAndFlush(task);
            response.put("status", "alright");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        }
        response.put("status", "nothing to change");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    public ResponseEntity<JsonNode> sendInCheck(int taskId) {
        ObjectNode response = jsonNodeFactory.objectNode();
        UserData currentUser = utilityController.getCurrentUser();
        Optional<TaskData> taskData = tasksDataRepository.findById(taskId);
        if (!taskData.isPresent()) {
            response.put("status", "task is not present");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        TaskData task = taskData.get();
        if (!currentUser.getId().equals(task.getInternId().getId())) {
            response.put("status", "access denied");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        task.setStatus("IN_CHECK");
        tasksDataRepository.saveAndFlush(task);
        response.put("status", "alright");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    public ResponseEntity<JsonNode> sendToRework(int taskId) {
        ObjectNode response = jsonNodeFactory.objectNode();
        UserData currentUser = utilityController.getCurrentUser();
        Optional<TaskData> taskData = tasksDataRepository.findById(taskId);
        if (!taskData.isPresent()) {
            response.put("status", "task is not present");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        TaskData task = taskData.get();
        if (!currentUser.getId().equals(task.getMentorId().getId())) {
            response.put("status", "access denied");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        task.setStatus("IN_WORK");
        tasksDataRepository.saveAndFlush(task);
        response.put("status", "alright");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    public ResponseEntity<JsonNode> completeTheTask(int taskId, JsonNode requestJson) {
        ObjectNode response = jsonNodeFactory.objectNode();
        UserData currentUser = utilityController.getCurrentUser();
        Optional<TaskData> taskData = tasksDataRepository.findById(taskId);
        if (!taskData.isPresent()) {
            response.put("status", "task is not present");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        TaskData task = taskData.get();
        if (!currentUser.getId().equals(task.getMentorId().getId())) {
            response.put("status", "access denied");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        task.setStatus("COMPLETED");
        tasksDataRepository.saveAndFlush(task);
        ReviewData reviewOnIntern = new ReviewData();
        reviewOnIntern.setTargetId(task.getInternId());
        reviewOnIntern.setTask(task);
        reviewOnIntern.setScoreFirst(requestJson.path("score_first").asDouble());
        reviewOnIntern.setScoreSecond(requestJson.path("score_second").asDouble());
        reviewOnIntern.setScoreThird(requestJson.path("score_third").asDouble());
        reviewOnIntern.setScoreFourth(requestJson.path("score_fourth").asDouble());
        reviewOnIntern.setScoreFifth(requestJson.path("score_fifth").asDouble());
        reviewOnIntern.setCreationDate(new Timestamp(System.currentTimeMillis()));
        reviewsDataRepository.saveAndFlush(reviewOnIntern);
        response.put("status", "alright");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    public ResponseEntity<JsonNode> allReviewsByUser(int userId) {
        UserData currentUser = utilityController.getCurrentUser();
        ObjectNode response = jsonNodeFactory.objectNode();
        if (!currentUser.getRole().equals("ADMIN") && !currentUser.getRole().equals("MENTOR")) {
            response.put("status", "access denied");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        Optional<UserData> userData = userDataService.findById(userId);
        if (!userData.isPresent()) {
            response.put("status", "user is not present");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        double totalScoreFirst = 0;
        double totalScoreSecond = 0;
        double totalScoreThird = 0;
        double totalScoreFourth = 0;
        double totalScoreFifth = 0;
        List<ReviewData> allReviewData = reviewsDataRepository.findAllByTargetId_Id(userId);
        List<ReturnedScore> returnedScores = new ArrayList<>();
        int amountReview = allReviewData.size();
        response.put("total_review", allReviewData.size());
        for (ReviewData allReviewDatum : allReviewData) {
            TaskData task = allReviewDatum.getTask();
            totalScoreFirst += allReviewDatum.getScoreFirst();
            totalScoreSecond += allReviewDatum.getScoreSecond();
            totalScoreThird += allReviewDatum.getScoreThird();
            totalScoreFourth += allReviewDatum.getScoreFourth();
            totalScoreFifth += allReviewDatum.getScoreFifth();
            returnedScores.add(new ReturnedScore(allReviewDatum.getScoreFirst(),
                    allReviewDatum.getScoreSecond(),
                    allReviewDatum.getScoreThird(),
                    allReviewDatum.getScoreFourth(),
                    allReviewDatum.getScoreFifth(), task.getId(),
                    task.getCreationDate().toString(),
                    task.getDeadlineDate().toString(),
                    task.getTaskDescription()));
        }
        ArrayNode arrayNode = new ObjectMapper().valueToTree(returnedScores);
        response.putArray("all_reviews").addAll(arrayNode);
        totalScoreFirst /= amountReview;
        totalScoreSecond /= amountReview;
        totalScoreThird /= amountReview;
        totalScoreFourth /= amountReview;
        totalScoreFifth /= amountReview;
        response.putObject("total_score")
                .put("total_score_first", totalScoreFirst)
                .put("total_score_second", totalScoreSecond)
                .put("total_score_third", totalScoreThird)
                .put("total_score_fourth", totalScoreFourth)
                .put("total_score_fifth", totalScoreFifth);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Transactional
    public ResponseEntity<JsonNode> removeTask(Integer taskId) {
        UserData currentUser = utilityController.getCurrentUser();
        ObjectNode response = jsonNodeFactory.objectNode();
        if (!currentUser.getRole().equals("MENTOR")) {
            response.put("status", "access denied");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        Optional<TaskData> task = tasksDataRepository.findById(taskId);
        if (!task.isPresent()) {
            response.put("status", "task is not present");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        tasksDataRepository.deleteById(taskId);
        response.put("status", "alright");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    public ResponseEntity<JsonNode> takeAllUserTasks(Integer userId) {
        Optional<UserData> intern = userDataService.findById(userId);
        ObjectNode response = jsonNodeFactory.objectNode();
        if (!intern.isPresent()) {
            response.put("status", "intern is not present");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        if (!intern.get().getRole().equals("INTERN")) {
            response.put("status", "this user is not intern");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        List<TaskData> allInternTask = tasksDataRepository.findAllByInternId_Id(userId);
        List<Integer> allInternTaskId = new ArrayList<Integer>();
        for (TaskData e : allInternTask) {
            allInternTaskId.add(e.getId());
        }
        ArrayNode arrayNode = new ObjectMapper().valueToTree(allInternTaskId.toArray());
        response.putArray("intern_tasks_id").addAll(arrayNode);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}
