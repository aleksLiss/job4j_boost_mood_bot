package ru.job4j.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.model.Award;

import java.util.List;

public interface AwardRepository extends CrudRepository<Award, Long> {

    void saveAll(List<Award> awardList);

    List<Award> findAll();

}
