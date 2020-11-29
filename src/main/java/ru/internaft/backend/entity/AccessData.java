package ru.internaft.backend.entity;

import lombok.Data;
import lombok.ToString;
import ru.internaft.backend.Roles;

import javax.persistence.*;

@Entity
@Table(name = "access_data")
@Data @ToString
public class AccessData {
        @Id
        @Column(name = "access_data")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;
        // email
        @Column(name = "email")
        private String email;

        @Column(name ="role")
        private String role;

}
