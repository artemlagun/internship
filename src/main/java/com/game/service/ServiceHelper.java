package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ServiceHelper {

    public static boolean validatePlayer(Player player) {

        if (player.getName() == null
                || player.getTitle() == null
                || player.getRace() == null
                || player.getProfession() == null
                || player.getBirthday() == null
                || player.getExperience() == null)
            return false;

        if (player.getBanned() == null) player.setBanned(false);
        if (!validatingFields(player)) return false;

        player.setUntilNextLevel(calculationUntilNextLevel(player));

        return true;
    }

    public static Integer calculationUntilNextLevel(Player player) {
        player.setLevel((int) (Math.sqrt(2500 + 200 * player.getExperience()) - 50) / 100);
        return (50 * (player.getLevel() + 1) * (player.getLevel() + 2) - player.getExperience());
    }

    public static Boolean validatingFields(Player player) {

        player.setExperience(player.getExperience());

        if (player.getName().length() > 12 || player.getTitle().length() > 30
                || player.getName().equals("") || player.getTitle().equals("")
                || player.getExperience() < 0 || player.getExperience() > 10_000_000
                || player.getBirthday().getYear() + 1900 < 2000 || player.getBirthday().getYear() + 1900 > 3001)
            return false;
        else return true;
    }

    public static List<Player> findByPagingCriteria(PlayerService playerService,
                                                    String name, String title, Race race, Profession profession,
                                                    Long after, Long before,
                                                    Boolean banned,
                                                    Integer minExperience, Integer maxExperience,
                                                    Integer minLevel, Integer maxLevel,
                                                    PlayerOrder order,
                                                    Integer pageNumber, Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page page = playerService.findAll(new Specification<Player>() {
            @Override
            public Predicate toPredicate(Root<Player> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (name != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("name"), "%" + name + "%")));
                }
                if (title != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("title"), "%" + title + "%")));
                }
                if (race != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("race"), race)));
                }
                if (profession != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("profession"), profession)));
                }
                if (after != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("birthday"), new Date(after))));
                }
                if (before != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.lessThan(root.get("birthday"), new Date(before))));
                }
                if (banned != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("banned"), banned)));
                }
                if (minExperience != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("experience"), minExperience)));
                }
                if (maxExperience != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("experience"), maxExperience)));
                }
                if (minLevel != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("level"), minLevel)));
                }
                if (maxLevel != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("level"), maxLevel)));
                }
                if (order != null) {
                    query.orderBy(criteriaBuilder.asc(root.get(order.getFieldName())));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }, pageable);

        return page.getContent();

    }

    public static Integer countByCriteria(PlayerService playerService,
                                          String name, String title, Race race, Profession profession,
                                          Long after, Long before,
                                          Boolean banned,
                                          Integer minExperience, Integer maxExperience,
                                          Integer minLevel, Integer maxLevel) {

        return (int) playerService.count(new Specification<Player>() {
            @Override
            public Predicate toPredicate(Root<Player> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (name != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("name"), "%" + name + "%")));
                }
                if (title != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("title"), "%" + title + "%")));
                }
                if (race != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("race"), race)));
                }
                if (profession != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("profession"), profession)));
                }
                if (after != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("birthday"), new Date(after))));
                }
                if (before != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.lessThan(root.get("birthday"), new Date(before))));
                }
                if (banned != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("banned"), banned)));
                }
                if (minExperience != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("experience"), minExperience)));
                }
                if (maxExperience != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("experience"), maxExperience)));
                }
                if (minLevel != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("level"), minLevel)));
                }
                if (maxLevel != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("level"), maxLevel)));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        });
    }
}
