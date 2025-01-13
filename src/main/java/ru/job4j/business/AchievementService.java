package ru.job4j.business;

import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import ru.job4j.content.Content;
import ru.job4j.event.MoodLogEvent;
import ru.job4j.model.User;
import ru.job4j.repository.AwardRepository;
import ru.job4j.repository.MoodLogRepository;
import ru.job4j.telegramm.api.SentContent;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AchievementService implements ApplicationListener<MoodLogEvent> {

    private final SentContent sentContent;
    private final AwardRepository awardRepository;
    private final MoodLogRepository moodLogRepository;

    public AchievementService(SentContent sentContent, AwardRepository awardRepository, MoodLogRepository moodLogRepository) {
        this.sentContent = sentContent;
        this.awardRepository = awardRepository;
        this.moodLogRepository = moodLogRepository;
    }

    @Transactional
    @Override
    public void onApplicationEvent(MoodLogEvent event) {
        User user = event.getMoodLog().getUser();
        AtomicInteger counterGoodMoodDays = new AtomicInteger();
        moodLogRepository.findByUserId(user.getChatId())
                .forEach(moodLog -> {
                    int i = moodLog.getMood().isGood()
                            ? counterGoodMoodDays.getAndIncrement()
                            : counterGoodMoodDays.getAndDecrement();
                });
        StringBuilder achievements = new StringBuilder();
        achievements = counterGoodMoodDays.get() < 1
                ? achievements.append("У вас еще нет достижений")
                : achievements.append("Ваши достижения: \n");
        if (counterGoodMoodDays.get() > 0) {
            StringBuilder achieves = new StringBuilder();
            awardRepository.findAll().stream()
                    .filter(award -> award.getDays() <= counterGoodMoodDays.get())
                    .forEach(
                            award -> {
                                achieves.append(award.getTitle())
                                        .append("\n")
                                        .append(award.getDescription())
                                        .append("\n");
                            }
                    );
            achievements.append(achieves);
        }
        Content content = new Content(user.getChatId());
        content.setText(achievements.toString());
        sentContent.sent(content);
    }
}
