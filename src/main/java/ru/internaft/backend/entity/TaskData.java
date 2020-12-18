package ru.internaft.backend.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tasks")
@Data
@ToString
public class TaskData {
    @Id
    @Column(name = "task_id")
    private int id;

    @ManyToOne()
    @JoinColumn(name = "intern_id")
    private UserData internId;

    @Column(name = "creation_date")
    private java.sql.Timestamp creationDate;

    @Column(name = "deadline_date")
    private java.sql.Timestamp deadlineDate;

    @Column(name = "task_description", columnDefinition = "TEXT")
    private String taskDescription;

    @Column(name = "status")
    private String status;

    @ManyToOne()
    @JoinColumn(name = "mentor_id")
    private UserData mentorId;

    @OneToOne(mappedBy = "task", cascade = CascadeType.ALL)
    private ReviewData review;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<FileData> file;

}
