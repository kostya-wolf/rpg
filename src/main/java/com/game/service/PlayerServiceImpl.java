package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.model.PlayerRequest;
import com.game.model.exception.BadRequestException;
import com.game.model.exception.NotFoundException;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static com.game.model.specification.PlayerSpecifications.*;

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
    public List<Player> getAllPlayers(PlayerRequest request) {
        return playerRepository.findAll(getSpecification(request), getPageable(request)).getContent();
    }

    @Override
    public long count(PlayerRequest request) {
        return playerRepository.count(getSpecification(request));
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
        Player player = getPlayerById(id);
        String name = request.getName();
        if (name != null) {
            validateName(name);
            player.setName(name);
        }
        String title = request.getTitle();
        if (title != null) {
            validateTitle(title);
            player.setTitle(title);
        }
        Race race = request.getRace();
        if (race != null) {
            player.setRace(race);
        }
        Profession profession = request.getProfession();
        if (profession != null) {
            player.setProfession(profession);
        }
        Long birthday = request.getBirthday();
        if (birthday != null) {
            validateBirthday(birthday);
            player.setBirthday(new Date(birthday));
        }
        Boolean banned = request.getBanned();
        if (banned != null) {
            player.setBanned(request.getBanned());
        }
        String experience = request.getExperience();
        if (experience != null) {
            validateExperience(experience);
            player.setExperience(Integer.parseInt(request.getExperience()));
        }
        setLevels(player);
        return playerRepository.save(player);
    }

    private Specification<Player> getSpecification(PlayerRequest request) {
        Specification<Player> result = (Specification<Player>) (root, query, criteriaBuilder)
                -> criteriaBuilder.isTrue(criteriaBuilder.literal(Boolean.TRUE));
        if (request.getName() != null) {
            result = result.and(containPartOfValueString("name", request.getName()));
        }
        if (request.getTitle() != null) {
            result = result.and(containPartOfValueString("title", request.getTitle()));
        }
        if (request.getAfter() != null || request.getBefore() != null) {
            result = result.and(betweenDates("birthday", request.getAfter(), request.getBefore()));
        }
        if (request.getMinExperience() != null || request.getMaxExperience() != null) {
            result = result.and(betweenNumbers("experience", request.getMinExperience(), request.getMaxExperience()));
        }
        if (request.getMinLevel() != null || request.getMaxLevel() != null) {
            result = result.and(betweenNumbers("level", request.getMinLevel(), request.getMaxLevel()));
        }
        if (request.getRace() != null) {
            result = result.and(withEnum("race", request.getRace()));
        }
        if (request.getProfession() != null) {
            result = result.and(withEnum("profession", request.getProfession()));
        }
        if (request.getBanned() != null) {
            result = result.and(withBoolean("banned", request.getBanned()));
        }
        /*

banned: false
         */

        return result;
    }

    private Pageable getPageable(PlayerRequest request) {
        PlayerOrder order = request.getOrder();
        Sort sort = order == null ? Sort.unsorted() : Sort.by(order.getFieldName());
        return PageRequest.of(request.getPageNumber(), request.getPageSize(), sort);
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
     * @param exp   опыт игрока
     * @return опыт до следующего уровня
     */
    private Integer calculateUntilNextLevel(Integer level, Integer exp) {
        return 50 * (level + 1) * (level + 2) - exp;
    }

    private void setLevels(Player player) {
        int level = calculateLevel(player.getExperience());
        player.setLevel(level);
        player.setUntilNextLevel(calculateUntilNextLevel(level, player.getExperience()));
    }
}
