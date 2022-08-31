package com.game.service;

import com.game.entity.Player;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service("playerService")
@Repository
@Transactional
public class PlayerServiceImpl implements PlayerService {
    private PlayerRepository playerRepository;

    @Autowired
    public void setPlayerRepository(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public Page<Player> findAll(Specification<Player> specification, Pageable pageable) {
        return playerRepository.findAll(specification, pageable);
    }

    @Override
    public long count(Specification<Player> specification) {
        return playerRepository.count(specification);
    }

    @Override
    public ResponseEntity<?> create(Player player) {
        if (!ServiceHelper.validatePlayer(player)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Player createPlayer = playerRepository.save(player);
        return new ResponseEntity<>(createPlayer, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> existById(String idString) {
        Long id;
        if ((id = validateId(idString)) == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(playerRepository.existsById(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> findById(String idString) {
        Long id;
        if ((id = validateId(idString)) == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<Player> player = playerRepository.findById(id);

        if (!player.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(player.get(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteById(String idString) {
        Long id;
        if ((id = validateId(idString)) == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (!playerRepository.existsById(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else {
            playerRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<?> update(String idString, Player player) {
        //Если id не валидный
        Long id;
        if ((id = validateId(idString)) == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        //Если элемента с таким id нет в базе
        if (!playerRepository.existsById(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else {
            Player playerFromBase = playerRepository.findById(id).get();

            //Если тело запроса пустое
            if (player.getName() == null && player.getTitle() == null
                    && player.getRace() == null && player.getProfession() == null
                    && player.getBanned() == null
                    && player.getBirthday() == null && player.getExperience() == null
                    && player.getUntilNextLevel() == null)
                //Возвращаем игрока из базы
                player = playerFromBase;
            else {
                //Если поля не валидные
                if (player.getExperience() != null) player.setExperience(player.getExperience());

                if ((player.getName() != null && (player.getName().length() > 12 || player.getName().equals("")))
                        || (player.getTitle() != null && (player.getTitle().length() > 30 || player.getTitle().equals("")))
                        || (player.getExperience() != null && (player.getExperience() < 0 || player.getExperience() > 10_000_000))
                        || (player.getBirthday() != null && (player.getBirthday().getYear() + 1900 < 2000 || player.getBirthday().getYear() + 1900 > 3001)))
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

                //Заменяем непустыми значениями
                if (player.getName() != null) playerFromBase.setName(player.getName());
                if (player.getTitle() != null) playerFromBase.setTitle(player.getTitle());
                if (player.getRace() != null) playerFromBase.setRace(player.getRace());
                if (player.getProfession() != null) playerFromBase.setProfession(player.getProfession());
                if (player.getBirthday() != null) playerFromBase.setBirthday(player.getBirthday());
                if (player.getBanned() != null) playerFromBase.setBanned(player.getBanned());
                if (player.getExperience() != null) playerFromBase.setExperience(player.getExperience());
                if (player.getLevel() != null) playerFromBase.setLevel(player.getLevel());
                playerFromBase.setUntilNextLevel(ServiceHelper.calculationUntilNextLevel(playerFromBase));

                playerRepository.save(playerFromBase);
            }

            return new ResponseEntity<>(playerFromBase, HttpStatus.OK);
        }
    }

    private Long validateId(String id) {
        try {
            Long idLong = Long.parseLong(id);
            if (idLong <= 0) return null;
            else return idLong;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
