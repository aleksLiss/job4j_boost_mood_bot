package ru.job4j.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.model.MoodLog;

public interface MoodLogRepository extends CrudRepository<MoodLog, Long> {
}
