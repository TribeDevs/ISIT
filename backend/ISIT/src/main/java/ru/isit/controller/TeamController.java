package ru.isit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.isit.dto.request.AddTeamRequest;
import ru.isit.dto.request.InviteUserInTeamRequest;
import ru.isit.dto.response.AddTeamResponse;
import ru.isit.dto.response.TeamResponse;
import ru.isit.models.CustomUserDetails;
import ru.isit.models.Team;
import ru.isit.service.TeamService;

import java.util.Collections;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/${api.version}/teams")
@RestController
public class TeamController {
    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<?> addTeam(@RequestBody AddTeamRequest request) {
        AddTeamResponse response = teamService.addTeam(request);

        return ResponseEntity.ok(response);
    }



    @GetMapping("/{id}")
    public ResponseEntity<?> getTeam(@PathVariable UUID id) {
        TeamResponse team = teamService.getTeam(id);

        return ResponseEntity.ok(team);
    }

    @PostMapping("/invite")
    public ResponseEntity<?> inviteUser(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody InviteUserInTeamRequest request) {
        teamService.inviteUser(userDetails, request);
        return ResponseEntity.ok(Collections.emptyMap());
    }

    @DeleteMapping("/{teamId}/users/{userId}")
    public ResponseEntity<?> removeUserFromTeam(@PathVariable UUID teamId, @PathVariable UUID userId) {
        teamService.removeUserFromTeam(teamId, userId);
        return ResponseEntity.ok(Collections.emptyMap());
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<?> removeTeam(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable UUID teamId) {
        teamService.removeTeam(userDetails, teamId);

        return ResponseEntity.ok(Collections.emptyMap());
    }

}
