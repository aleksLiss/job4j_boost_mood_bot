package ru.job4j.business;

import org.springframework.stereotype.Service;
import ru.job4j.content.Content;
import ru.job4j.model.MoodLog;
import ru.job4j.model.User;
import ru.job4j.recommedations.RecommendationEngine;
import ru.job4j.repository.AchievementRepository;
import ru.job4j.repository.MoodLogRepository;
import ru.job4j.repository.UserRepository;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class MoodService {

    private final MoodLogRepository moodLogRepository;
    private final RecommendationEngine recommendationEngine;
    private final UserRepository userRepository;
    private final AchievementRepository achievementRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("dd-MM-yyyy HH:mm")
            .withZone(ZoneId.systemDefault());

    public MoodService(MoodLogRepository moodLogRepository,
                       RecommendationEngine recommendationEngine,
                       UserRepository userRepository,
                       AchievementRepository achievementRepository) {
        this.moodLogRepository = moodLogRepository;
        this.recommendationEngine = recommendationEngine;
        this.userRepository = userRepository;
        this.achievementRepository = achievementRepository;
    }

    public Content chooseMood(User user, Long moodId) {
        return recommendationEngine.recommendFor(user.getChatId(), moodId);
    }

    public Optional<Content> weekMoodLogCommand(long chatId, Long clientId) {
        return Optional.of(new Content(chatId));
    }

    public Optional<Content> monthMoodLogCommand(long chatId, Long clientId) {
        return Optional.of(new Content(chatId));
    }

    private String formatMoodLogs(List<MoodLog> logs, String title) {
        StringBuilder result = new StringBuilder(title).append(":\n");
        if (!logs.isEmpty()) {
            logs.forEach(log -> {
                String formattedDate = formatter.format(Instant.ofEpochSecond(log.getCreatedAt()));
                result.append(formattedDate).append(": ").append(log.getMood().getText()).append("\n");
            });
        }
        return !logs.isEmpty()
                ? result.toString()
                : new StringBuilder()
                .append(title)
                .append(":\n")
                .append("No mood logs found.").toString();
    }

    public Optional<Content> awards(long chatId, Long clientId) {
        return Optional.of(new Content(chatId));
    }
}