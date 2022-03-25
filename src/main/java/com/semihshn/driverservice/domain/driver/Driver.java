package com.semihshn.driverservice.domain.driver;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class Driver {

    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birhDate;
}
