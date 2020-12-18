package ru.internaft.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.internaft.backend.entity.UserData;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersDataRepository extends JpaRepository<UserData, Integer> {
    UserData findByEmail(String string);

    Optional<UserData> findById(Integer id);

    List<UserData> findAllByRole(String role);

    void deleteById(Integer id);
}
