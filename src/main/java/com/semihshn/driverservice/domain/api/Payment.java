package com.semihshn.driverservice.domain.api;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigInteger;

@Setter
@Getter
@Builder
public class Payment {

    private Long userId;
    private String cvv;

    private String expireDate;

    @Enumerated(EnumType.STRING)
    private CardType cardType;
    private String ccNo;
    private BigInteger amount;

    public enum CardType {
        DEBIT, CREDIT
    }
}
