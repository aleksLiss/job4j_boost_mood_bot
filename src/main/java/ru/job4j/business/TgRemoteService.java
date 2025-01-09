package ru.job4j.business;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.job4j.model.User;
import ru.job4j.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

@Service
public class TgRemoteService extends TelegramLongPollingBot {

    private final String botName;
    private final String botToken;
    private static Map<String, String> moodResp = new HashMap<>();
    private final UserRepository userRepository;

    public TgRemoteService(@Value("${telegram.bot.name}") String botName,
                           @Value("${telegram.bot.token}") String botToken,
                           UserRepository userRepository) {
        this.botName = botName;
        this.botToken = botToken;
        putDefaultAnswers();
        this.userRepository = userRepository;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            if ("/start".equals(message.getText())) {
                long chatId = message.getChatId();
                var user = new User();
                user.setClientId(message.getFrom().getId());
                user.setChatId(chatId);
                userRepository.add(user);
            }
        }

        if (update.hasCallbackQuery()) {
            var data = update.getCallbackQuery().getData();
            var chatId = update.getCallbackQuery().getMessage().getChatId();
            sendMessage(new SendMessage(String.valueOf(chatId), moodResp.get(data)));
        }
    }

    public void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException ex) {
            ex.printStackTrace();
        }
    }

    private void putDefaultAnswers() {
        moodResp.put("lost_sock", "Носки — это коварные создания. Но не волнуйся, второй обязательно найдётся!");
        moodResp.put("cucumber", "Огурец тоже дело серьёзное! Главное, не мариноваться слишком долго.");
        moodResp.put("dance_ready", "Супер! Танцуй, как будто никто не смотрит. Или, наоборот, как будто все смотрят!");
        moodResp.put("need_coffee", "Кофе уже в пути! Осталось только подождать... И ещё немного подождать...");
        moodResp.put("sleepy", "Пора на боковую! Даже супергерои отдыхают, ты не исключение.");
    }

    @Override
    public String getBotUsername() {
        return botName;
    }
}
