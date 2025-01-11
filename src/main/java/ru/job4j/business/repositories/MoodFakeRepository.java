package ru.job4j.business.repositories;

import org.springframework.test.fake.CrudRepositoryFake;
import ru.job4j.model.Mood;
import ru.job4j.repository.MoodRepository;

import java.util.ArrayList;
import java.util.List;

public class MoodFakeRepository extends CrudRepositoryFake<Mood, Long> implements MoodRepository {

    @Override
    public List<Mood> findAll() {
        return new ArrayList<>(memory.values());
    }

}
