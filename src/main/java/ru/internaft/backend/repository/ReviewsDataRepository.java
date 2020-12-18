package ru.internaft.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.internaft.backend.entity.MentorshipData;
import ru.internaft.backend.entity.ReviewData;

import java.util.List;

public interface ReviewsDataRepository extends JpaRepository<ReviewData, Integer> {
    ReviewData[] findAllByTargetId_Id(int id);
}
