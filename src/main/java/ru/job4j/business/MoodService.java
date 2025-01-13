package ru.job4j.business;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ru.job4j.content.Content;
import ru.job4j.event.MoodLogEvent;
import ru.job4j.model.*;
import ru.job4j.recommedations.RecommendationEngine;
import ru.job4j.repository.*;

import java.time.Instant;
import java.time.LocalDateTime;
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
    private final MoodRepository moodRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("dd-MM-yyyy HH:mm")
            .withZone(ZoneId.systemDefault());
    private final ApplicationEventPublisher applicationEventPublisher;

    public MoodService(MoodLogRepository moodLogRepository,
                       RecommendationEngine recommendationEngine,
                       UserRepository userRepository,
                       AchievementRepository achievementRepository,
                       MoodRepository moodRepository,
                       ApplicationEventPublisher applicationEventPublisher) {
        this.moodLogRepository = moodLogRepository;
        this.recommendationEngine = recommendationEngine;
        this.userRepository = userRepository;
        this.achievementRepository = achievementRepository;
        this.moodRepository = moodRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public Content chooseMood(User user, Long moodId) {
        moodRepository.findById(moodId)
                .ifPresent(value -> {
                    MoodLog moodLog = new MoodLog(user, value, System.currentTimeMillis());
                    applicationEventPublisher.publishEvent(new MoodLogEvent(this, moodLog));
                    moodLogRepository.save(moodLog);
                });
        return recommendationEngine.recommendFor(user.getChatId(), moodId);
    }

    public Optional<Content> weekMoodLogCommand(long chatId, Long clientId) {
        List<MoodLog> logs = filteredMoodLogList(clientId)
                .stream()
                .filter(moodLog -> LocalDateTime
                        .ofInstant(Instant
                                .ofEpochSecond(moodLog.getCreatedAt()), ZoneId.systemDefault()
                        ).isBefore(LocalDateTime.now().minusDays(7)))
                .toList();
        return Optional.of(new Content(chatId, formatMoodLogs(logs, "week moods log")));
    }

    public Optional<Content> monthMoodLogCommand(long chatId, Long clientId) {
        List<MoodLog> logs = filteredMoodLogList(clientId)
                .stream()
                .filter(moodLog -> LocalDateTime
                        .ofInstant(Instant
                                .ofEpochSecond(moodLog.getCreatedAt()), ZoneId.systemDefault()
                        ).isBefore(LocalDateTime.now().minusDays(30)))
                .toList();
        return Optional.of(new Content(chatId, formatMoodLogs(logs, "month moods log")));
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
        List<Achievement> awards = achievementRepository
                .findAll()
                .stream()
                .filter(achievement -> achievement
                        .getUser()
                        .getId()
                        .equals(clientId))
                .toList();
        StringBuilder awardsString = new StringBuilder();
        if (!awards.isEmpty()) {
            awardsString.append("Awards list: \n");
            awards.forEach(achievement -> {
                String formattedDAte = formatter.format(Instant.ofEpochSecond(achievement.getCreateAt()));
                awardsString
                        .append(formattedDAte)
                        .append(": ")
                        .append(achievement.getAward().getTitle())
                        .append("\n")
                        .append("description: ")
                        .append(achievement.getAward().getDescription());
            });
        } else {
            awardsString.append("You have not yet earned your awards");
        }
        return Optional.of(new Content(chatId, awardsString.toString()));
    }

    private List<MoodLog> filteredMoodLogList(Long clientId) {
        return moodLogRepository
                .findAll()
                .stream()
                .filter(moodLog -> moodLog.getId().equals(clientId)).toList();
    }
}