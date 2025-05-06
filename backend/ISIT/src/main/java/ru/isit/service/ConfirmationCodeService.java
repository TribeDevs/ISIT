package ru.isit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.isit.exception.Exception;
import ru.isit.models.ConfirmationCode;
import ru.isit.repository.ConfirmationCodeRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ConfirmationCodeService {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random random = new Random();

    private final ConfirmationCodeRepository confirmationCodeRepository;


    public static String generate(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    public String saveConfirmationCode(String email) {
        String code = generate(6);

        ConfirmationCode confirmationCode = new ConfirmationCode(
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(5),
                email,
                code
        );

        confirmationCodeRepository.save(confirmationCode);

        return code;
    }

    public boolean validateCode(String email, String code) {
        Optional<ConfirmationCode> optionalCode = confirmationCodeRepository.findByCodeAndEmail(code, email);

        if (optionalCode.isEmpty()) {
            throw new Exception("Код не найден");
        }

        ConfirmationCode confirmationCode = optionalCode.get();

        if (confirmationCode.isUsed()) {
            throw new Exception("Код уже был использован");
        }

        if (LocalDateTime.now().isAfter(confirmationCode.getExpiresAt())) {
            throw new Exception("Срок действия кода истек");
        }

        confirmationCode.setUsed(true);
        confirmationCodeRepository.save(confirmationCode);
        return true;
    }

    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void purgeExpiredCodes() {
        confirmationCodeRepository.deleteByExpiresAtBefore(LocalDateTime.now());
    }
}