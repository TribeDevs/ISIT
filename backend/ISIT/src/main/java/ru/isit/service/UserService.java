package ru.isit.service;


import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.isit.dto.request.LoginRequest;
import ru.isit.dto.response.UserResponse;
import ru.isit.exception.Exception;
import ru.isit.models.Role;
import ru.isit.models.User;
import ru.isit.repository.UserRepository;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void grantRole(UUID userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        user.getRoles().add(role);
        userRepository.save(user);
    }

    @Transactional
    public void revokeRole(UUID userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        boolean removed = user.getRoles().remove(role);
        if (!removed) {
            throw new Exception("User  does not have role: " + role);
        }

        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public UserResponse getUserById(UUID userId) {
        Optional<User> user = userRepository.findById(userId);
        UserResponse response = new UserResponse(
                user.get().getId(),
                user.get().getEmail(),
                user.get().getUsername(),
                user.get().getRoles(),
                user.get().getAvatarUrl(),
                user.get().isVerified(),
                user.get().getCreatedAt()
        );
        return response;
    }

    public void setAvatar(UUID userId, String filePath) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setAvatarUrl(filePath);
        userRepository.save(user);
    }

    @Transactional
    public Boolean deleteUser(UUID userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return true;
        }

        return false;
    }

    @Transactional
    public boolean verifyUser(UUID id, LoginRequest request) {
        HttpClient httpClient = HttpClient.newBuilder()
                .cookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ALL))
                .build();
        try {
            HttpClient client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .build();

            Map<String, String> formData = Map.of(
                    "login", request.getUsername(),
                    "password", request.getPassword()
            );

            String formBody = formData.entrySet().stream()
                    .map(entry -> URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "=" +
                            URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                    .collect(Collectors.joining("&"));

            HttpRequest getRequest = HttpRequest.newBuilder()
                    .uri(URI.create("https://users.sfu-kras.ru/php/auth.php"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(formBody))
                    .build();

            HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                return false;
            }
            System.out.println("Response: " + response.body());
            JSONObject json = new JSONObject(response.body());
            String result = json.getString("data");

            if (response.statusCode() == 200 && "SUCCESS".equals(result)) {
                User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
                user.setVerified(true);
                userRepository.save(user);
                return true;
            }


            return false;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при проверке авторизации", e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}