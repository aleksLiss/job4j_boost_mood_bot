package ru.job4j.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.model.Achievement;

public interface AchievementRepository extends CrudRepository<Achievement, Long> {
}
