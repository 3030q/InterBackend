package ru.internaft.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.internaft.backend.entity.TaskData;
import ru.internaft.backend.entity.UserData;

import java.util.List;
import java.util.Optional;

public interface TasksDataRepository extends JpaRepository<TaskData, Integer> {
    Optional<TaskData> findById(Integer id);
    void deleteById(Integer id);
    List<TaskData> findAllByInternId_Id(Integer id);
}
