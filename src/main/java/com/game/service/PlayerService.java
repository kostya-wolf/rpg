package com.game.service;

import com.game.entity.Player;

import java.util.List;

/**
 * Интерфейс сервиса для работы с PlayerRepository
 */
public interface PlayerService {
    List<Player> getAllPlayers();

}
