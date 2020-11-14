package ru.internaft.backend.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity @Table(name = "login_data")
@Data @ToString
public class LoginData {
    @Id @Column(name = "login")
    // email
    private String login;

    @Column(name = "bcrypt_pswd")
    private String password; // bcrypt_hash

    @Column(name = "user_id")
    private Integer id;
}
