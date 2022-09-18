package com.semihshn.driverservice.adapter.rest.driver.request;

import com.semihshn.driverservice.domain.driver.Driver;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class DriverUpdateRequest {

    @NotNull
    private Long id;

    @NotNull
    private Long userId;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private LocalDate birhDate;

    public Driver convertToDriver() {
        return Driver.builder()
                .id(id)
                .userId(userId)
                .firstName(firstName)
                .lastName(lastName)
                .birthDate(birhDate)
                .build();
    }
}
