package ru.isit.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class TeamResponse {
    private UUID id;
    private String name;
    private UUID creatorId;
    private String logoUrl;
    private int gameId;
    private LocalDateTime createdAt;
    private List<Map<String, Object>> users;
}