package com.game.repository;

import com.game.entity.Player;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Интерфейс репозитория для работы с сущностями Player
 */
public interface PlayerRepository extends PagingAndSortingRepository<Player, Long>, JpaSpecificationExecutor<Player> {
}
