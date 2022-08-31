package com.game.service;

import com.game.entity.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;

public interface PlayerService {

    Page findAll(Specification<Player> specification, Pageable pageable);

    long count(Specification<Player> specification);

    ResponseEntity<?> create(Player player);

    ResponseEntity<?> existById(String id);

    ResponseEntity<?> findById(String id);

    ResponseEntity<?> deleteById(String id);

    ResponseEntity<?> update(String id, Player player);
}

