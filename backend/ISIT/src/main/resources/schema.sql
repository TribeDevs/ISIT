CREATE TABLE IF NOT EXISTS users (
                                     id UUID PRIMARY KEY,
                                     username VARCHAR(50) UNIQUE NOT NULL,
                                     password VARCHAR(255) NOT NULL,
                                     email VARCHAR(100) UNIQUE NOT NULL,
                                     avatar_url VARCHAR(255),
                                     verified BOOL,
                                     created_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_roles (
                                          user_id UUID,
                                          role VARCHAR(15)
);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_user_roles_user
        FOREIGN KEY (user_id)
            REFERENCES users(id)
            ON DELETE CASCADE;

CREATE TABLE IF NOT EXISTS codes (
                                      id UUID PRIMARY KEY,
                                      email VARCHAR(100),
                                      code VARCHAR,
                                      created_at TIMESTAMP,
                                      expires_at TIMESTAMP,
                                      used BOOLEAN
);

CREATE TABLE IF NOT EXISTS bind_platform (
                                             id UUID PRIMARY KEY,
                                             user_id UUID NOT NULL,
                                             platform VARCHAR(255) UNIQUE NOT NULL
);

ALTER TABLE bind_platform
    ADD CONSTRAINT fk_bind_platform_user
        FOREIGN KEY (user_id)
            REFERENCES users(id)
            ON DELETE CASCADE;

CREATE TABLE IF NOT EXISTS teams (
                                     id UUID PRIMARY KEY,
                                     name VARCHAR(25) UNIQUE NOT NULL,
                                     creator_id UUID NOT NULL,
                                     logo_url VARCHAR(255),
                                     created_at TIMESTAMP
);

ALTER TABLE teams
    ADD CONSTRAINT fk_teams_creator
        FOREIGN KEY (creator_id)
            REFERENCES users(id)
            ON DELETE CASCADE;

CREATE TABLE IF NOT EXISTS team_members (
                                            team_id UUID,
                                            user_id UUID,
                                            PRIMARY KEY (team_id, user_id)
);

ALTER TABLE team_members
    ADD CONSTRAINT fk_team_members_user
        FOREIGN KEY (user_id)
            REFERENCES users(id)
            ON DELETE CASCADE;

ALTER TABLE team_members
    ADD CONSTRAINT fk_team_members_team
        FOREIGN KEY (team_id)
            REFERENCES teams(id)
            ON DELETE CASCADE;

CREATE TABLE IF NOT EXISTS games (
                                     id BIGSERIAL PRIMARY KEY,
                                     name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS tournaments (
                                           id UUID PRIMARY KEY,
                                           name VARCHAR(100) NOT NULL,
                                           game_id INTEGER NOT NULL,
                                           organizer_id UUID NOT NULL,
                                           winner_team UUID,
                                           start_date TIMESTAMP,
                                           end_date TIMESTAMP,
                                           status VARCHAR(50)
);

ALTER TABLE tournaments
    ADD CONSTRAINT fk_tournaments_game
        FOREIGN KEY (game_id)
            REFERENCES games(id)
            ON DELETE CASCADE;

ALTER TABLE tournaments
    ADD CONSTRAINT fk_tournaments_organizer
        FOREIGN KEY (organizer_id)
            REFERENCES users(id)
            ON DELETE CASCADE;

CREATE TABLE IF NOT EXISTS registratitons (
                                              id UUID PRIMARY KEY,
                                              tournament_id UUID NOT NULL,
                                              team_id UUID NOT NULL,
                                              registrated_at TIMESTAMP,
                                              status VARCHAR(50)
);

ALTER TABLE registratitons
    ADD CONSTRAINT fk_registrations_tournament
        FOREIGN KEY (tournament_id)
            REFERENCES tournaments(id)
            ON DELETE CASCADE;

ALTER TABLE registratitons
    ADD CONSTRAINT fk_registrations_team
        FOREIGN KEY (team_id)
            REFERENCES teams(id)
            ON DELETE CASCADE;

CREATE TABLE IF NOT EXISTS match_participants (
                                                  match_id UUID,
                                                  team_id UUID,
                                                  score INTEGER,
                                                  PRIMARY KEY (match_id, team_id)
);

CREATE TABLE matches (
                         id UUID PRIMARY KEY,
                         tournament_id UUID NOT NULL,
                         name VARCHAR(100) NOT NULL,
                         status VARCHAR(50),
                         win_id UUID NOT NULL,
                         start_time TIMESTAMP
);

ALTER TABLE matches
    ADD CONSTRAINT fk_matches_tournament
        FOREIGN KEY (tournament_id)
            REFERENCES tournaments(id)
            ON DELETE CASCADE;

ALTER TABLE matches
    ADD CONSTRAINT fk_matches_win_id
        FOREIGN KEY (win_id)
            REFERENCES teams(id)
            ON DELETE CASCADE;