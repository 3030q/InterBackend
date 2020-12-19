package ru.internaft.backend.SpringAppCache;

public class ReturnedScore {
    private double scoreFirst;
    private double scoreSecond;
    private double scoreThird;
    private double scoreFourth;
    private double scoreFifth;
    private Integer taskId;
    private String creationDate;
    private String deadlineDate;
    private String taskDescription;

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public void setDeadlineDate(String deadlineDate) {
        this.deadlineDate = deadlineDate;
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

    public ReturnedScore(double scoreFirst,
                         double scoreSecond,
                         double scoreThird,
                         double scoreFourth,
                         double scoreFifth,
                         Integer taskId,
                         String creationDate,
                         String deadlineDate,
                         String taskDescription) {
        this.scoreFirst = scoreFirst;
        this.scoreSecond = scoreSecond;
        this.scoreThird = scoreThird;
        this.scoreFourth = scoreFourth;
        this.scoreFifth = scoreFifth;
        this.taskId = taskId;
        this.creationDate = creationDate;
        this.deadlineDate = deadlineDate;
        this.taskDescription = taskDescription;
    }

    public ReturnedScore(double scoreFirst, double scoreSecond, double scoreThird, double scoreFourth, double scoreFifth) {
        this.scoreFirst = scoreFirst;
        this.scoreSecond = scoreSecond;
        this.scoreThird = scoreThird;
        this.scoreFourth = scoreFourth;
        this.scoreFifth = scoreFifth;
    }

    public void setScoreSecond(double scoreSecond) {
        this.scoreSecond = scoreSecond;
    }

    public void setScoreThird(double scoreThird) {
        this.scoreThird = scoreThird;
    }

    public void setScoreFourth(double scoreFourth) {
        this.scoreFourth = scoreFourth;
    }

    public void setScoreFifth(double scoreFifth) {
        this.scoreFifth = scoreFifth;
    }

    public void setScoreFirst(double scoreFirst) {
        this.scoreFirst = scoreFirst;
    }

    public double getScoreFirst() {
        return scoreFirst;
    }

    public double getScoreSecond() {
        return scoreSecond;
    }

    public double getScoreThird() {
        return scoreThird;
    }

    public double getScoreFourth() {
        return scoreFourth;
    }

    public double getScoreFifth() {
        return scoreFifth;
    }
}
