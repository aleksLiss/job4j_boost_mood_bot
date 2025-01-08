package ru.job4j.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.model.MoodContent;

import java.util.List;

public interface MoodContentRepository extends CrudRepository<MoodContent, Long> {

    void saveAll(List<MoodContent> moodContentList);

    List<MoodContent> findAll();
}
