package com.semihshn.driverservice.adapter.rest.driver.request;

import com.semihshn.driverservice.domain.driver.Driver;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
public class DriverCreateRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private LocalDate birhDate;

    public Driver convertToDriver() {
        return Driver.builder()
                .firstName(firstName)
                .lastName(lastName)
                .birhDate(birhDate)
                .build();
    }
}
