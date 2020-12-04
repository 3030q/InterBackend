package ru.internaft.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.internaft.backend.entity.FileData;

import java.util.Optional;

@Repository
public interface FileDataRepository extends JpaRepository<FileData, Integer> {
    Optional<FileData> findById(Integer id);
    FileData findByPath(String string);
    void deleteById(Integer id);
}
