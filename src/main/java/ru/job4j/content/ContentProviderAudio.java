package ru.job4j.content;

import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;

public class ContentProviderAudio implements ContentProvider {
    @Override
    public Content byMood(Long chatId, Long moodId) {
        Content content = new Content(chatId);
        content.setAudio(new InputFile(new File("./audio/music.mp3")));
        return content;
    }
}
