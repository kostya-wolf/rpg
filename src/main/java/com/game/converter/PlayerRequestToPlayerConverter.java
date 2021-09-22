package com.game.converter;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.model.PlayerRequest;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

public class PlayerRequestToPlayerConverter implements Converter<PlayerRequest, Player> {
    @Override
    public Player convert(PlayerRequest request) {
        Player player = new Player();
        String name = request.getName();
        if (name != null) {
            player.setName(name);
        }
        String title = request.getTitle();
        if (title != null) {
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
            player.setBirthday(new Date(birthday));
        }
        player.setBanned(request.isBanned());
        String experience = request.getExperience();
        if (experience != null) {
            player.setExperience(Integer.parseInt(request.getExperience()));
        }
        setLevels(player);
        return player;
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

    private void setLevels(Player player) {
        int level = calculateLevel(player.getExperience());
        player.setLevel(level);
        player.setUntilNextLevel(calculateUntilNextLevel(level, player.getExperience()));
    }
}
