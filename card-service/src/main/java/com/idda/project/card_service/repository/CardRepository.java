package com.idda.project.card_service.repository;

import com.idda.project.card_service.entity.Card;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findByUserId(Long userId);

    Optional<Card> findByEncryptedCardNumber(String encryptedCardNumber);

    Optional<Card> findByUserIdAndIsActive(Long userId, boolean isActive);

    void deleteByUserId(Long userId);
}
