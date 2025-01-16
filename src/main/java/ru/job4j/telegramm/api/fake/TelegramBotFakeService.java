package ru.job4j.telegramm.api.fake;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.job4j.condition.OnFakeCondition;
import ru.job4j.content.Content;
import ru.job4j.printer.ConsolePrinter;
import ru.job4j.printer.Printer;
import ru.job4j.telegramm.api.BotCommandHandler;
import ru.job4j.telegramm.api.SentContent;

@Component
@Conditional(OnFakeCondition.class)
public class TelegramBotFakeService extends TelegramLongPollingBot implements SentContent {

    private final BotCommandHandler handler;
    private final String botName;
    private Printer printer;

    public TelegramBotFakeService(@Value("${telegram.bot.name}") String botName,
                                  @Value("${telegram.bot.token}") String botToken,
                                  BotCommandHandler handler) {
        super(botToken);
        this.handler = handler;
        this.botName = botName;
        this.printer = new ConsolePrinter();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            handler.handleCallback(update.getCallbackQuery()).ifPresent(this::sent);
        } else if (update.hasMessage() && update.getMessage().getText() != null) {
            handler.commands(update.getMessage()).ifPresent(this::sent);
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void sent(Content content) {
        if (content.getText() != null) {
            printer.print(content.getText());
        }
    }
}
