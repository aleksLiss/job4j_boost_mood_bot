package ru.job4j.telegramm.api;

public class SentContentException extends RuntimeException {

    public SentContentException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
