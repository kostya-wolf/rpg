package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.model.exception.NotFoundException;
import com.game.model.PlayerRequest;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest")
public class RpgController {

    private PlayerService playerService;

    @Autowired
    public RpgController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/players")
    public List<Player> getPlayers() {
        return playerService.getAllPlayers();
    }

    @GetMapping("/players/count")
    public long getPlayersCount() {
        return playerService.count();
    }

    @PostMapping("/players")
    public Player getNewPlayer(@RequestBody PlayerRequest playerRequest) {
        return playerService.createPlayer(playerRequest);
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
