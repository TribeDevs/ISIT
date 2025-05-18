package ru.isit.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.isit.dto.request.AddTeamRequest;
import ru.isit.dto.response.AddTeamResponse;
import ru.isit.models.Team;
import ru.isit.service.TeamService;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class TeamController {
    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<?> addTeam(@RequestBody AddTeamRequest request) {
        AddTeamResponse response = teamService.addTeam(request);

        return ResponseEntity.ok(response);
    }

}
