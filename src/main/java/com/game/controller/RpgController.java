package com.game.controller;

import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.exception.NotFoundException;
import com.game.model.Player;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/rest")
public class RpgController {

    @GetMapping("/players")
    public List<Player> getPlayers() {
        Player player = new Player();
        player.setId(1L);
//        player.setName("Happy new year");
        player.setProfession(Profession.PALADIN);
//        player.setBanned(false);
//        player.setBirthday(new Date().getTime());
//        player.setExperience(250);
//        player.setLevel(11);
        player.setRace(Race.ELF);
//        player.setTitle("Новый год");
//        player.setUntilNextLevel(1);
        return Collections.singletonList(player);
    }

    @GetMapping("/players/count")
    public Integer getPlayersCount() {
        return 1;
    }

    @PostMapping("/players")
    public Player getNewPlayer() {
        Player player = new Player();
        player.setId(2L);
        player.setProfession(Profession.WARRIOR);
        player.setRace(Race.HUMAN);
        return player;
    }

    @GetMapping("/players/{id}")
    public Player getPlayerById(@PathVariable("id") long id) {
        throw new NotFoundException("Не корректный id: " + id);
    }

    @PostMapping("/players/{id}")
    public Player updatePlayer(@PathVariable("id") long id) {
        Player player = new Player();
        player.setId(2L);
        player.setProfession(Profession.DRUID);
        player.setRace(Race.HUMAN);
        return player;
    }

    @DeleteMapping("/players/{id}")
    public void deletePlayer(@PathVariable("id") long id) {
        throw new NotFoundException("Не корректный id: " + id);
    }
}
