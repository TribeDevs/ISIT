package ru.isit.service;


import lombok.RequiredArgsConstructor;
import org.json.JSONException;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
        try {
            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

            HttpClient client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .cookieHandler(cookieManager)
                    .build();

            Map<String, String> formData = Map.of(
                    "login", request.getUsername(),
                    "password", request.getPassword()
            );

            String formBody = formData.entrySet().stream()
                    .map(entry -> URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "=" +
                            URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                    .collect(Collectors.joining("&"));

            HttpRequest loginRequest = HttpRequest.newBuilder()
                    .uri(URI.create("https://users.sfu-kras.ru/php/auth.php"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .POST(HttpRequest.BodyPublishers.ofString(formBody))
                    .build();

            HttpResponse<String> loginResponse = client.send(loginRequest, HttpResponse.BodyHandlers.ofString());

            if (loginResponse.statusCode() != 200) {
                return false;
            }

            JSONObject authJson = new JSONObject(loginResponse.body());
            if (!"SUCCESS".equalsIgnoreCase(authJson.optString("data", ""))) {
                return false;
            }

            HttpRequest infoRequest = HttpRequest.newBuilder()
                    .uri(URI.create("https://users.sfu-kras.ru/php/login_info.php"))
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .GET()
                    .build();

            HttpResponse<String> infoResponse = client.send(infoRequest, HttpResponse.BodyHandlers.ofString());
            String html = infoResponse.body();

            Document doc = Jsoup.parse(html);

            Element fioEl = doc.selectFirst("div.fioview");
            if (fioEl == null) {
                System.err.println("ФИО не найдено. HTML:\n" + doc.html());
                return false;
            }
            String fullName = fioEl.text().split("\\(")[0].trim();

            Element groupEl = doc.selectFirst("td.profile_title:containsOwn(Группа) + td");
            if (groupEl == null) {
                System.err.println("Группа не найдена. HTML:\n" + doc.html());
                return false;
            }
            String group = groupEl.text();

            Element instituteEl = doc.selectFirst("td.profile_title:containsOwn(Институт) + td");
            if (instituteEl == null) {
                System.err.println("Институт не найден. HTML:\n" + doc.html());
                return false;
            }
            String institute = instituteEl.text();

            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found: " + id));

            user.setVerified(true);
            user.setSfuName(fullName);
            user.setSfuGroup(group);
            user.setSfuInstitute(institute);
            userRepository.save(user);

            return true;

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при верификации: " + e.getMessage(), e);
        } catch (JSONException e) {
            throw new RuntimeException("Ошибка парсинга JSON: " + e.getMessage(), e);
        }
    }
}