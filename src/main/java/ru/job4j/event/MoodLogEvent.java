package ru.job4j.event;

import org.springframework.context.ApplicationEvent;
import ru.job4j.model.MoodLog;

public class MoodLogEvent extends ApplicationEvent {

    private final MoodLog moodLog;

    public MoodLogEvent(Object source, MoodLog moodLog) {
        super(source);
        this.moodLog = moodLog;
    }

    public MoodLog getMoodLog() {
        return moodLog;
    }
}
