package ru.job4j.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.model.MoodLog;

import java.util.List;

public interface MoodLogRepository extends CrudRepository<MoodLog, Long> {
    List<MoodLog> findAll();

    void saveAll(List<MoodLog> moods);
}
