package ru.isit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.isit.dto.request.AddTeamRequest;
import ru.isit.dto.response.AddTeamResponse;
import ru.isit.models.Team;
import ru.isit.repository.TeamRepository;

import java.util.Random;

@RequiredArgsConstructor
@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random random = new Random();

    @Transactional
    public AddTeamResponse addTeam(AddTeamRequest request) {
        Team team = new Team();
        team.setName(team.getName());
        team.setCountMembers(team.getCountMembers());
        team.setCreatorId(request.getCreatorId());
        team.setGameId(request.getGameId());
        teamRepository.save(team);

        AddTeamResponse response = new AddTeamResponse();
        response.setInviteLink(createInviteLink());
        return response;
    }

    private String createInviteLink() {
        StringBuilder sb = new StringBuilder(24);
        for (int i = 0; i < 24; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
}
