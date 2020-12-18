package ru.internaft.backend.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "reviews")
@Data
@ToString
public class ReviewData {
    @Id
    @Column(name = "review_id")
    private int reviewId;

    @OneToOne
    @JoinColumn(name = "task_id", referencedColumnName = "task_id")
    private TaskData task;

    @Column(name = "score_first")
    private double scoreFirst;

    @Column(name = "score_second")
    private double scoreSecond;

    @Column(name = "score_third")
    private double scoreThird;

    @Column(name = "score_fourth")
    private double scoreFourth;

    @Column(name = "score_fifth")
    private double scoreFifth;

    @Column(name = "creation_date")
    private java.sql.Timestamp creationDate;

    @ManyToOne()
    @JoinColumn(name = "target_id")
    private UserData targetId;


}
