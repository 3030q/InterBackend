package ru.internaft.backend.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "mentorship_data")
@Data
@ToString
public class MentorshipData {
    @Id
    @Column(name = "mentorship_id")
    private int id;

    @OneToOne()
    @JoinColumn(name = "mentorship_intern_id", referencedColumnName = "user_id")
    private UserData internId;

    @ManyToOne()
    @JoinColumn(name = "mentorship_mentor_id", nullable = false)
    private UserData mentorId;
}
