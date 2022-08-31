package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import com.game.service.ServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PlayerController {
    private PlayerService playerService;

    @Autowired
    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping(value = "/rest/players")
    public ResponseEntity<?> getAllBy(@RequestParam(value = "name", required = false) String name,
                                      @RequestParam(value = "title", required = false) String title,
                                      @RequestParam(value = "race", required = false) Race race,
                                      @RequestParam(value = "profession", required = false) Profession profession,
                                      @RequestParam(value = "after", required = false) Long after,
                                      @RequestParam(value = "before", required = false) Long before,
                                      @RequestParam(value = "banned", required = false) Boolean banned,
                                      @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                      @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                      @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                      @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
                                      @RequestParam(value = "order", required = false) PlayerOrder order,
                                      @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
                                      @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize) {

        List<Player> playerList = ServiceHelper.findByPagingCriteria(playerService,
                name, title, race, profession,
                after, before,
                banned,
                minExperience, maxExperience,
                minLevel, maxLevel,
                order,
                pageNumber, pageSize);

        return new ResponseEntity<>(playerList, HttpStatus.OK);
    }

    @GetMapping(value = "/rest/players/count")
    public ResponseEntity<?> getCount(@RequestParam(value = "name", required = false) String name,
                                      @RequestParam(value = "title", required = false) String title,
                                      @RequestParam(value = "race", required = false) Race race,
                                      @RequestParam(value = "profession", required = false) Profession profession,
                                      @RequestParam(value = "after", required = false) Long after,
                                      @RequestParam(value = "before", required = false) Long before,
                                      @RequestParam(value = "banned", required = false) Boolean banned,
                                      @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                      @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                      @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                      @RequestParam(value = "maxLevel", required = false) Integer maxLevel) {

        int count = ServiceHelper.countByCriteria(playerService,
                name, title, race, profession,
                after, before,
                banned,
                minExperience, maxExperience,
                minLevel, maxLevel);

        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @PostMapping(value = "/rest/players")
    public ResponseEntity<?> createPlayer(@RequestBody Player player) {
        return playerService.create(player);
    }

    @GetMapping(value = "/rest/players/{id}")
    public ResponseEntity<?> findPlayerById(@PathVariable String id) {
        return playerService.findById(id);
    }

    @DeleteMapping(value = "/rest/players/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        return playerService.deleteById(id);
    }

    @PostMapping(value = "/rest/players/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Player player) {
        ResponseEntity<?> responseEntity = playerService.update(id, player);
        Player player1 = (Player) responseEntity.getBody();
        return responseEntity;
    }
}
