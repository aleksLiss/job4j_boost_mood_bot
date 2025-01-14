package ru.job4j.business;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import ru.job4j.business.repositories.MoodFakeRepository;
import ru.job4j.model.Mood;
import ru.job4j.repository.MoodRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration(classes = {TgUI.class, MoodFakeRepository.class, Mood.class})
class TgUITest {
    @Autowired
    @Qualifier("moodFakeRepository")
    private MoodRepository moodRepository;
    @MockBean
    Mood mood;

    @BeforeEach
    public void setUpRepository() {
        moodRepository = new MoodFakeRepository();
    }

    @Test
    public void whenMoodSaveThenReturnMood() {
        assertThat(moodRepository.save(mood).getId()).isZero();
        assertThat(moodRepository.save(mood).getText()).isEqualTo(null);
        assertThat(moodRepository.save(mood).isGood()).isFalse();
    }

    @Test
    public void whenFindAllThenReturnOneMood() {
        moodRepository.save(mood);
        assertThat(moodRepository.findAll().get(0)).isNotNull().isEqualTo(mood);
    }

    @Test
    public void whenDeleteAllThenFindAllReturnListWithLengthZero() {
        moodRepository.save(mood);
        moodRepository.deleteAll();
        assertThat(moodRepository.findAll()).isEmpty();
    }

    @Test
    public void whyFindAllReturnMoodWithIdEqualsZeroButFindByIdReturnOptionalWithEmptyValue() {
        moodRepository.save(mood);
        moodRepository.findAll().forEach(i -> System.out.println(i.getId()));
        assertThat(moodRepository.findById(0L)).isEqualTo(Optional.empty());
    }
}
