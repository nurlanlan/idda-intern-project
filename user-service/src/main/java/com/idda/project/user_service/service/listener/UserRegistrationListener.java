package com.idda.project.user_service.service.listener;

import com.idda.project.user_service.domain.entity.User;
import com.idda.project.user_service.domain.event.UserRegisteredEvent;
import com.idda.project.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserRegistrationListener {
    private final UserRepository userRepository;

    @RabbitListener(queues = "user.registration.queue")
    public void handleUserRegistration(UserRegisteredEvent event) {
        log.info("Received user registration event: {}", event.getUserId());

        try {

            User userProfile = new User();
            userProfile.setId(event.getUserId());
            userProfile.setFullName(event.getFullName());

            userRepository.save(userProfile);
            log.info("User profile created in UserService for userId: {}", event.getUserId());

        } catch (Exception e) {
            log.error("Error processing user registration event for userId: {}. Error: {}", event.getUserId(), e.getMessage());
        }

    }
}
