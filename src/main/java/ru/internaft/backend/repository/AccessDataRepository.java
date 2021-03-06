package ru.internaft.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.internaft.backend.entity.AccessData;

@Repository
public interface AccessDataRepository extends JpaRepository<AccessData, Integer> {
    AccessData findByEmail(String string);

    AccessData findById(int id);

    Integer deleteByEmail(String string);
}
