package ru.internaft.backend.SpringAppCache;

public class ReturnedTask {
    private Integer taskId;
    private String creationDate;
    private String deadlineDate;
    private String taskDescription;


    public void setDeadlineDate(String deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getDeadlineDate() {
        return deadlineDate;
    }

    public String getTaskDescription() {
        return taskDescription;
    }


    public ReturnedTask(Integer taskId, String creationDate, String deadlineDate, String taskDescription) {
        this.taskId = taskId;
        this.creationDate = creationDate;
        this.deadlineDate = deadlineDate;
        this.taskDescription = taskDescription;
    }
}
