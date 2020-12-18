package ru.internaft.backend.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "token_data")
@Data
public class TokenData {
    @Id
    @Column(name = "token")
    private String token;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "creation_timestamp")
    private Timestamp creationTimestamp;
}
    