package ru.internaft.backend.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity @Table(name = "login_data")
@Data @ToString
public class LoginData {
    @Id
    @Column(name = "login")
    private String login;

    @Column(name = "bcrypt_pswd")
    private String password; // bcrypt_hash

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserData userData;

}
