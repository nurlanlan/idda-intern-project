package com.idda.project.card_service.repository;

import com.idda.project.card_service.entity.Card;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findByUserId(Long userId);

    Optional<Card> findByEncryptedCardNumber(String encryptedCardNumber);

    @Modifying
    @Query("UPDATE Card c SET c.balance = c.balance - :amount WHERE c.id = :cardId AND c.balance >= :amount")
    int debitBalance(Long cardId, float amount);

    Optional<Card> findByUserIdAndIsActive(Long userId, boolean isActive);

    void deleteByUserId(Long userId);
}
