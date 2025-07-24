package entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.YearMonth;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Data
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String encryptedCardNumber;

    @Column(nullable = false)
    private String encryptedCvv;

    @Column(nullable = false)
    private float balance;

    @Column(nullable = false)
    private boolean isPrimary;

    @Column(nullable = false)
    private boolean isActive;

    @Column(nullable = false)
    private YearMonth expirationDate;


}
