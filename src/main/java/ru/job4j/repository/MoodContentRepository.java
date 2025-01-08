package ru.job4j.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.model.MoodContent;

public interface MoodContentRepository extends CrudRepository<MoodContent, Long> {
}
