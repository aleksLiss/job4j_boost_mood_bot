package ru.job4j.business;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.job4j.model.User;
import ru.job4j.repository.UserRepository;

@Component
public class ReminderService implements BeanNameAware {

    private String beanName;
    private final UserRepository userRepository;

    public ReminderService(String beanName, UserRepository userRepository) {
        this.beanName = beanName;
        this.userRepository = userRepository;
    }

    @Scheduled(fixedRateString = "${remind.period}")
    public void ping() {
        for (User user : userRepository.findAll()) {
            SendMessage message = new SendMessage();
            message.setChatId(user.getChatId());
            message.setText("Ping");
        }
    }

    @PostConstruct
    public void init() {
        System.out.println("Bean is going through @PostConstruct init.");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Bean will be destroyed via @PreDestroy.");
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    public void displayBeanName() {
        System.out.println(beanName);
    }
}
