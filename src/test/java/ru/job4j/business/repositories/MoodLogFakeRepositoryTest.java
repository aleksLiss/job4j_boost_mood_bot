package ru.job4j.business.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.model.Mood;
import ru.job4j.model.MoodLog;
import ru.job4j.model.User;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

class MoodLogFakeRepositoryTest {

    private MoodLogFakeRepository moodLogFakeRepository;

    @BeforeEach
    public void setUp() {
        moodLogFakeRepository = new MoodLogFakeRepository();
    }

    @Test
    public void whenFindAllThenReturnSomeMoodLogs() {
        User user = new User();
        MoodLog moodLog = new MoodLog(user,
                new Mood("Счастливейший на свете \uD83D\uDE0E", true),
                System.currentTimeMillis());
        moodLog.setId(1L);
        MoodLog moodLog2 = new MoodLog(user,
                new Mood("Усталое настроение \uD83D\uDE34", false),
                System.currentTimeMillis());
        moodLog2.setId(2L);
        moodLogFakeRepository.saveAll(Arrays.asList(moodLog, moodLog2));
        assertThat(moodLogFakeRepository.findAll())
                .isNotEmpty()
                .hasSize(2)
                .containsExactlyInAnyOrder(moodLog2, moodLog);
    }

    @Test
    public void whenFindByUserIdThenReturnOneMoodLog() {
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        MoodLog moodLog1 = new MoodLog(user1,
                new Mood("Счастливейший на свете \uD83D\uDE0E", true),
                System.currentTimeMillis());
        moodLog1.setId(1L);
        MoodLog moodLog2 = new MoodLog(user2,
                new Mood("Усталое настроение \uD83D\uDE34", false),
                System.currentTimeMillis());
        moodLog2.setId(2L);
        moodLogFakeRepository.saveAll(Arrays.asList(moodLog1, moodLog2));
        assertThat(moodLogFakeRepository.findByUserId(1L))
                .isNotEmpty()
                .hasSize(1)
                .containsExactlyInAnyOrder(moodLog1);
    }

    @Test
    public void whenSaveSomeMoodLogsThenReturnSomeMoodLogsInOrder() {
        User user1 = new User();
        user1.setId(1L);
        MoodLog moodLog1 = new MoodLog(user1,
                new Mood("Счастливейший на свете \uD83D\uDE0E", true),
                System.currentTimeMillis() - 100000L);
        moodLog1.setId(1L);
        MoodLog moodLog2 = new MoodLog(user1,
                new Mood("Усталое настроение \uD83D\uDE34", false),
                System.currentTimeMillis() - 10L);
        moodLog2.setId(2L);
        moodLogFakeRepository.saveAll(Arrays.asList(moodLog1, moodLog2));
        assertThat(moodLogFakeRepository.findByUserIdOrderByCreatedAtDesc(1L))
                .isNotEmpty()
                .hasSize(2)
                .containsExactly(moodLog2, moodLog1);
    }

    @Test
    public void whenOneUserDidNotVoteTodayThenReturnOneUser() {
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        long startOfDay = LocalDate.now()
                .minusDays(1)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        long date = LocalDate.now()
                .minusDays(3)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        long date2 = LocalDate.now()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        MoodLog moodLog1 = new MoodLog(user1,
                new Mood("Счастливейший на свете \uD83D\uDE0E", true),
                date);
        moodLog1.setId(1L);
        MoodLog moodLog2 = new MoodLog(user2,
                new Mood("Усталое настроение \uD83D\uDE34", false),
                date2);
        moodLog2.setId(2L);
        moodLogFakeRepository.saveAll(Arrays.asList(moodLog1, moodLog2));
        assertThat(moodLogFakeRepository.findUsersWhoDidNotVoteToday(startOfDay))
                .isNotEmpty()
                .hasSize(1)
                .containsExactly(user1);
    }

    @Test
    public void whenSaveSomeMoodLogsThenReturnMoodLogsForWeek() {
        User user1 = new User();
        user1.setId(1L);
        long weekStart = LocalDate.now()
                .minusDays(7)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        long date = LocalDate.now()
                .minusDays(3)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        long date2 = LocalDate.now()
                .minusDays(10)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        MoodLog moodLog1 = new MoodLog(user1,
                new Mood("Счастливейший на свете \uD83D\uDE0E", true),
                date);
        moodLog1.setId(1L);
        MoodLog moodLog2 = new MoodLog(user1,
                new Mood("Усталое настроение \uD83D\uDE34", false),
                date2);
        moodLog2.setId(2L);
        moodLogFakeRepository.saveAll(Arrays.asList(moodLog1, moodLog2));
        assertThat(moodLogFakeRepository.findMoodLogsForWeek(1L, weekStart))
                .isNotEmpty()
                .hasSize(1)
                .containsExactly(moodLog1);
    }

    @Test
    public void whenSaveSomeMoodLogsThenReturnMoodLogsForMonth() {
        User user1 = new User();
        user1.setId(1L);
        long weekStart = LocalDate.now()
                .minusDays(30)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        long date = LocalDate.now()
                .minusDays(10)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        long date2 = LocalDate.now()
                .minusDays(40)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        MoodLog moodLog1 = new MoodLog(user1,
                new Mood("Счастливейший на свете \uD83D\uDE0E", true),
                date);
        moodLog1.setId(1L);
        MoodLog moodLog2 = new MoodLog(user1,
                new Mood("Усталое настроение \uD83D\uDE34", false),
                date2);
        moodLog2.setId(2L);
        moodLogFakeRepository.saveAll(Arrays.asList(moodLog1, moodLog2));
        assertThat(moodLogFakeRepository.findMoodLogsForWeek(1L, weekStart))
                .isNotEmpty()
                .hasSize(1)
                .containsExactly(moodLog1);

    }
}