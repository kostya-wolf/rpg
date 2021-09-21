package com.game.service;

import com.game.entity.Player;
import com.game.model.PlayerRequest;
import com.game.model.exception.BadRequestException;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

/**
 * Реализация интерфейса сервиса для работы с PlayerRepository
 */
@Service
public class PlayerServiceImpl implements PlayerService {
    PlayerRepository playerRepository;
    ConversionService conversionService;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, ConversionService conversionService) {
        this.playerRepository = playerRepository;
        this.conversionService = conversionService;
    }

    @Override
    public List<Player> getAllPlayers() {
        return playerRepository.findAll((Specification<Player>) null);
    }

    @Override
    public long count() {
        return playerRepository.count();
    }

    @Override
    public Player createPlayer(PlayerRequest request) {
        validatePlayerRequest(request);
        Player player = conversionService.convert(request, Player.class);
        int level = calculateLevel(player.getExperience());
        player.setLevel(level);
        player.setUntilNextLevel(calculateUntilNextLevel(level, player.getExperience()));
        return playerRepository.save(player);
    }

    private void validatePlayerRequest(PlayerRequest request) {
        if (request == null
                || StringUtils.isEmpty(request.getName())
                || request.getTitle() == null
                || request.getRace() == null
                || request.getProfession() == null
                || request.getBirthday() == null
                || request.getExperience() == null) {
            throw new BadRequestException();
        }
        validateParamByLength(request.getName(), 12);
        validateParamByLength(request.getTitle(), 30);
        validateExperience(request.getExperience());
        validateBirthday(request.getBirthday());
        validateRegisterDate();
    }

    private void validateParamByLength(String param, int maxLength) {
        if (param.length() > maxLength) {
            throw new BadRequestException(String.format("Длина параметра %s превышает %d символов", param, maxLength));
        }
    }

    private void validateExperience(String experience) {
        int exp;
        try {
            exp = Integer.parseInt(experience);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Параметр опыт (experience) должен быть числом");
        }
        if (exp < 0 || exp > 10000000) {
            throw new BadRequestException("Опыт находится вне заданных пределов");
        }
    }

    private void validateBirthday(Long birthday) {
        if (birthday < 0) {
            throw new BadRequestException("Неправильная дата рождения: < 0");
        }
    }

    private void validateRegisterDate() {
        LocalDate localDate = LocalDate.now();
        if (localDate.getYear() < 2000 || localDate.getYear() > 3000) {
            throw new BadRequestException("Дата регистрации находится вне заданных пределов");
        }
    }

    /**
     * Метод рассчитывает уровень игрока
     *
     * @param exp опыт игрока
     * @return уровень игрока
     */
    private int calculateLevel(int exp) {
        return (int) ((Math.sqrt(2500 + 200 * exp) - 50) / 100);
    }

    /**
     * Метод рассчитывает опыт до следующего уровня
     *
     * @param level уровень игрока
     * @param exp опыт игрока
     * @return опыт до следующего уровня
     */
    private Integer calculateUntilNextLevel(Integer level, Integer exp) {
        return 50 * (level + 1) * (level + 2) - exp;
    }
}
