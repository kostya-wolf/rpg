package com.game.service;

import com.game.entity.Player;
import com.game.model.PlayerRequest;

import java.util.List;

/**
 * Интерфейс сервиса для работы с PlayerRepository
 */
public interface PlayerService {
    List<Player> getAllPlayers(PlayerRequest request);

    long count(PlayerRequest playerRequest);

    Player createPlayer(PlayerRequest playerRequest);

    Player getPlayerById(long id);

    void deletePlayer(long id);

    Player updatePlayer(long id, PlayerRequest request);
}
