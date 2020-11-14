package ru.internaft.backend.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity @Table(name = "user_data")
@Data @ToString
public class UserData {
    @Id @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "about_text")
    private String aboutText;

    @Column(name = "role")
    private Integer role;
}
