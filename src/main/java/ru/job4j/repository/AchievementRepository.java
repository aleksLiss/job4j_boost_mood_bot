package ru.job4j.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.model.Achievement;

import java.util.List;

public interface AchievementRepository extends CrudRepository<Achievement, Long> {

    void saveAll(List<Achievement> awardList);

    List<Achievement> findAll();
}
