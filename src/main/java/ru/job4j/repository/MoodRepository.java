package ru.job4j.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.model.Mood;

import java.util.List;
import java.util.stream.Stream;

public interface MoodRepository extends CrudRepository<Mood, Long> {
    List<Mood> findAll();

    void saveAll(List<Mood> moods);
}
