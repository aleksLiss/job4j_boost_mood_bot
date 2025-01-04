package ru.job4j.content;

public class ContentProviderText implements ContentProvider {
    @Override
    public Content byMood(Long chatId, Long moodId) {
        Content content = new Content(chatId);
        content.setText("Text");
        return content;
    }
}
