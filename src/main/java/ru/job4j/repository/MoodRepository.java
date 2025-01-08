package ru.job4j.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.model.Mood;

public interface MoodRepository extends CrudRepository<Mood, Long> {
}
