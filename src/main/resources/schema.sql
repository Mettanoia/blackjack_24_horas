CREATE TABLE players
(
    id    BIGINT              NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name  VARCHAR(255)        NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE games
(
    id          BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    game_result VARCHAR(50)
);

CREATE TABLE game_participation
(
    id        BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    game_id   BIGINT NOT NULL,
    player_id BIGINT NOT NULL,
    FOREIGN KEY (game_id) REFERENCES games (id) ON DELETE CASCADE,
    FOREIGN KEY (player_id) REFERENCES players (id) ON DELETE CASCADE
);

CREATE TABLE game_winners
(
    id        BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    game_id   BIGINT NOT NULL,
    player_id BIGINT NOT NULL,
    FOREIGN KEY (game_id) REFERENCES games (id) ON DELETE CASCADE,
    FOREIGN KEY (player_id) REFERENCES players (id) ON DELETE CASCADE
);
