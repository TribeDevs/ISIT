package ru.isit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.isit.models.Team;

import java.util.UUID;

@Repository
public interface TeamRepository extends JpaRepository<Team, UUID> {
}
