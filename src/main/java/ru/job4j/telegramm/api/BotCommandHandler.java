package ru.job4j.telegramm.api;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.job4j.business.MoodService;
import ru.job4j.business.TgUI;
import ru.job4j.content.Content;
import ru.job4j.model.User;
import ru.job4j.repository.UserRepository;

import java.util.Optional;

@Service
public class BotCommandHandler {
    private final UserRepository userRepository;
    private final MoodService moodService;
    private final TgUI tgUI;

    public BotCommandHandler(UserRepository userRepository, MoodService moodService, TgUI tgUI) {
        this.userRepository = userRepository;
        this.moodService = moodService;
        this.tgUI = tgUI;
    }

    public Optional<Content> commands(Message message) {
        Optional<Content> result;
        switch (message.getText()) {
            case "/start":
                result = handleStartCommand(message.getChatId(), message.getFrom().getId());
                break;
            case "/week_mood_log":
                result = moodService.weekMoodLogCommand(message.getChatId(), message.getFrom().getId());
                break;
            case "/month_mood_log":
                result = moodService.monthMoodLogCommand(message.getChatId(), message.getFrom().getId());
                break;
            case "/award":
                result = moodService.awards(message.getChatId(), message.getFrom().getId());
                break;
            default:
                result = Optional.empty();
                break;
        }
        return result;
    }

    public Optional<Content> handleCallback(CallbackQuery callback) {
        Long moodId = Long.valueOf(callback.getData());
        User user = userRepository.findByClientId(callback.getFrom().getId());
        return Optional.of(moodService.chooseMood(user, moodId));
    }

    private Optional<Content> handleStartCommand(long chatId, Long clientId) {
        User user = new User();
        user.setClientId(clientId);
        user.setChatId(chatId);
        userRepository.save(user);
        Content content = new Content(user.getChatId());
        content.setText("Как настроение?");
        content.setMarkup(tgUI.buildButtons());
        return Optional.of(content);
    }
}
