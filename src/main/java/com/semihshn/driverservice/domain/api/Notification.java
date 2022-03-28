package com.semihshn.driverservice.domain.api;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Builder
public class Notification {

    private Long driverId;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String telephoneAddress;
    private String message;
}
