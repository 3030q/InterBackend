package ru.internaft.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.internaft.backend.entity.MentorshipData;

import java.util.List;

public interface MentorshipDataRepository extends JpaRepository<MentorshipData, Integer> {
    void deleteByInternId_Id(int internId);

    MentorshipData findByInternId_Id(int id);

    List<MentorshipData> findAllByMentorId_Id(int id);
}
