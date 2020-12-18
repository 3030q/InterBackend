package ru.internaft.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.internaft.backend.entity.TaskData;
import ru.internaft.backend.entity.TokenData;

@Repository
public interface TokensDataRepository extends JpaRepository<TaskData, Integer> {

}
