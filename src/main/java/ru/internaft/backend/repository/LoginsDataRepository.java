package ru.internaft.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.internaft.backend.entity.LoginData;

@Repository
public interface LoginsDataRepository extends JpaRepository<LoginData, String> {
    LoginData findByLogin(String login);
}
