package com.game.converter;

import com.game.entity.Player;
import com.game.model.PlayerRequest;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

public class PlayerRequestToPlayerConverter implements Converter<PlayerRequest, Player> {
    @Override
    public Player convert(PlayerRequest request) {
        Player result = new Player();
        result.setBanned(request.getBanned() == null ? false : request.getBanned());
        result.setBirthday(new Date(request.getBirthday()));
        result.setExperience(Integer.parseInt(request.getExperience()));
        result.setName(request.getName());
        result.setProfession(request.getProfession());
        result.setRace(request.getRace());
        result.setTitle(request.getTitle());
        return result;
    }
}
