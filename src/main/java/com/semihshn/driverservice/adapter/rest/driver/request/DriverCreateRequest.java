package com.semihshn.driverservice.adapter.rest.driver.request;

import com.semihshn.driverservice.domain.driver.Driver;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class DriverCreateRequest {

    @NotNull
    private Long userId;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private LocalDate birhDate;

    public Driver convertToDriver() {
        return Driver.builder()
                .userId(userId)
                .firstName(firstName)
                .lastName(lastName)
                .birhDate(birhDate)
                .build();
    }
}
