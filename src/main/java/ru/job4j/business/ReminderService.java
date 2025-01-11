package ru.job4j.business;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.job4j.content.Content;
import ru.job4j.model.User;
import ru.job4j.repository.MoodLogRepository;
import ru.job4j.telegramm.api.SentContent;

import java.time.LocalDate;
import java.time.ZoneId;

@Service
public class ReminderService {

    private final SentContent sentContent;
    private final MoodLogRepository moodLogRepository;
    private final TgUI tgUI;

    public ReminderService(SentContent sentContent, MoodLogRepository moodLogRepository, TgUI tgUI) {
        this.sentContent = sentContent;
        this.moodLogRepository = moodLogRepository;
        this.tgUI = tgUI;
    }

    @Scheduled(fixedRateString = "${recommendation.alert.period}")
    public void remindUser() {
        long startOfDay = LocalDate
                .now()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        for (User user : moodLogRepository.findUsersWhoDidNotVoteToday(startOfDay)) {
            Content content = new Content(user.getChatId());
            content.setText("Как настроение?");
            content.setMarkup(tgUI.buildButtons());
            sentContent.sent(content);
        }
    }
}
