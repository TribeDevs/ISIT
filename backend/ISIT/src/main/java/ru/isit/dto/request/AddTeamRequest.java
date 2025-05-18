package ru.isit.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class AddTeamRequest {
    private String name;
    private int countMembers;
    private UUID creatorId;
    private int gameId;

}
