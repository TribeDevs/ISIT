package ru.isit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.isit.dto.request.AddTeamRequest;
import ru.isit.dto.request.InviteUserInTeamRequest;
import ru.isit.dto.response.AddTeamResponse;
import ru.isit.dto.response.TeamResponse;
import ru.isit.models.CustomUserDetails;
import ru.isit.models.Team;
import ru.isit.repository.TeamRepository;

import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random random = new Random();

    public TeamResponse getTeam(UUID id) {
        Optional<Team> team = teamRepository.findById(id);

        TeamResponse teamResponse = new TeamResponse();
        teamResponse.setId(team.get().getId());
        teamResponse.setName(team.get().getName());
        teamResponse.setUsers(team.get().getUsers());
        teamResponse.setCreatorId(team.get().getCreatorId());
        teamResponse.setLogoUrl(team.get().getLogoUrl());
        teamResponse.setCreatedAt(team.get().getCreatedAt());

        return teamResponse;
    }

    @Transactional
    public AddTeamResponse addTeam(AddTeamRequest request) {
        Team team = new Team();
        team.setName(request.getName());
        team.setCreatorId(request.getCreatorId());
        team.setLogoUrl(request.getLogoUrl());
        team.setGameId(request.getGameId());
        team.setCountMembers(request.getCountMembers());
        team.setCreatedAt(LocalDateTime.now());

        List<Map<String, Object>> users = new ArrayList<>();
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", request.getCreatorId());
        users.add(userMap);

        team.setUsers(users);

        String code = generateInviteCode();
        AddTeamResponse response = new AddTeamResponse();
        response.setInviteCode(code);

        team.setInviteCode(code);
        teamRepository.save(team);


        return response;
    }

    @Transactional
    public void inviteUser(CustomUserDetails userDetails, InviteUserInTeamRequest request) {
        Team team = teamRepository.findByInviteCode(request.getCode())
                .orElseThrow(() -> new RuntimeException("Team not found"));

        boolean alreadyExists = team.getUsers().stream()
                .anyMatch(map -> Objects.equals(map.get("id"), userDetails.getId()));

        if (alreadyExists) return;

        if (team.getUsers().size() >= team.getCountMembers()) {
            throw new RuntimeException("Team is full");
        }

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", userDetails.getId());
        team.getUsers().add(userMap);

        teamRepository.save(team);
    }

    @Transactional
    public void removeUserFromTeam(UUID teamId, UUID userId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        if (team.getCreatorId().equals(userId)) {
            throw new RuntimeException("You cannot remove the team creator");
        }

        boolean removed = team.getUsers().removeIf(map ->
                Objects.equals(UUID.fromString(map.get("id").toString()), userId)
        );

        if (!removed) {
            throw new RuntimeException("User not found in team");
        }

        teamRepository.save(team);
    }

    @Transactional
    public void removeTeam(CustomUserDetails userDetails, UUID teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        if (!team.getCreatorId().equals(userDetails.getId())) {
            throw new RuntimeException("Only the team creator can delete the team");
        }

        teamRepository.deleteById(teamId);
    }



    private String generateInviteCode() {
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}
