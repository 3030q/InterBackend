package ru.internaft.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.internaft.backend.entity.User;

@Repository
public interface UsersRepository extends JpaRepository<User, Integer> {
}
