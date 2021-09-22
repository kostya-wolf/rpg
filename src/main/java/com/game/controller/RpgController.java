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
        return playerService.getPlayerById(id);
    }

    @PostMapping("/players/{id}")
    public Player updatePlayer(@PathVariable("id") long id, @RequestBody PlayerRequest playerRequest) {
        return playerService.updatePlayer(id, playerRequest);
    }

    @DeleteMapping("/players/{id}")
    public void deletePlayer(@PathVariable("id") long id) {
        playerService.deletePlayer(id);
    }
}
