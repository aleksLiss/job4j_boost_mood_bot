package ru.job4j.content;

import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;

public class ContentProviderImage implements ContentProvider {

    @Override
    public Content byMood(Long chatId, Long moodId) {
        Content content = new Content(chatId);
        content.setPhoto(new InputFile(new File("./images/logo.png")));
        return content;
    }
}
