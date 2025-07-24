package dto.response;

import java.time.YearMonth;

public class CardResponse {
    private Long id;
    private String maskedCardNumber;
    private YearMonth expirationDate;
    private float balance;
    private boolean isActive;

}
