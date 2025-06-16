package ru.isit.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.isit.dto.UserInTeamDto;

import java.util.List;
import java.util.UUID;

@Data
public class AddTeamRequest {
    private String name;
    private UUID creatorId;
    private String logoUrl;
    private int gameId;
    private int countMembers;
}
