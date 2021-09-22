package com.game.service;

import com.game.entity.Player;
import com.game.model.PlayerRequest;
import com.game.model.exception.BadRequestException;
import com.game.model.exception.NotFoundException;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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
        return playerRepository.save(player);
    }

    @Override
    public Player getPlayerById(long id) {
        validateId(id);
        return playerRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    @Override
    public void deletePlayer(long id) {
        Player player = getPlayerById(id);
        playerRepository.delete(player);
    }

    @Override
    public Player updatePlayer(long id, PlayerRequest request) {
        String name = request.getName();
        if (name != null) {
            validateName(name);
        }
        String title = request.getTitle();
        if (title != null) {
            validateTitle(title);
        }
        Long birthday = request.getBirthday();
        if (birthday != null) {
            validateBirthday(birthday);
        }
        String experience = request.getExperience();
        if (experience != null) {
            validateExperience(experience);
        }
        Player player = conversionService.convert(request, Player.class);
        return playerRepository.save(player);
    }

    private void validateName(String name) {
        if (name.isEmpty()) {
            throw new BadRequestException("Пустая строка name");
        }
        validateParamByLength(name, 12);
    }

    private void validateTitle(String title) {
        validateParamByLength(title, 30);
    }

    private void validatePlayerRequest(PlayerRequest request) {
        if (request == null
                || request.getName() == null
                || request.getTitle() == null
                || request.getRace() == null
                || request.getProfession() == null
                || request.getBirthday() == null
                || request.getExperience() == null) {
            throw new BadRequestException();
        }
        validateName(request.getName());
        validateTitle(request.getTitle());
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

    private void validateId(long id) {
        if (id < 1) {
            throw new BadRequestException("Не корректный id: " + id);
        }
    }
}
