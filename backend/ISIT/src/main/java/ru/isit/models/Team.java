package ru.isit.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "teams")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID creatorId;
    private String name;
    private String logoUrl;
    private int countMembers;
    private int gameId;
    private LocalDateTime createdAt;

}
