package ru.internaft.backend.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "file_data")
@Data
@ToString
public class FileData {
    @Id
    @Column(name = "file_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path")
    private String path;

    @Column(name = "type")
    private String type;

    @OneToOne(mappedBy = "avatarData")
    private UserData userData;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private TaskData task;
}
