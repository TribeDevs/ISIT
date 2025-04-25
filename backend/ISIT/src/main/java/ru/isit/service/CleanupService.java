package ru.isit.service;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.isit.repository.UserRepository;


@Service
@AllArgsConstructor
public class CleanupService {
    private final UserRepository userRepository;

    @Scheduled(cron = "0 0 */4 * * ?")
    @Transactional
    public void purgeOldData() {
        userRepository.deleteByEnableFalse();
    }
}