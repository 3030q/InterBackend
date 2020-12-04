package ru.internaft.backend.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user_data")
@Data
@ToString
public class UserData {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "about_text")
    private String aboutText;

    @Column(name = "role")
    private String role;

    @OneToOne(mappedBy = "userData")
    private LoginData loginData;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "avatar_id", referencedColumnName = "file_id")
    private FileData avatarData;

    @OneToOne(mappedBy = "internId")
    private MentorshipData mentorshipDataForIntern;

    @OneToMany(mappedBy = "mentorId")
    private List<MentorshipData> mentorshipDataForMentor;
}
