package ru.internaft.backend.repository;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.internaft.backend.entity.LoginData;

import java.util.Optional;

@Repository
public interface LoginsDataRepository extends JpaRepository<LoginData, String> {
    LoginData findByLogin(String login);
}
