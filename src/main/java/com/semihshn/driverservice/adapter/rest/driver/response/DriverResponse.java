package com.semihshn.driverservice.adapter.rest.driver.response;

import com.semihshn.driverservice.domain.driver.Driver;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverResponse {
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private LocalDate birhDate;

    public static DriverResponse from(Driver driver) {
        return DriverResponse.builder()
                .id(driver.getId())
                .userId(driver.getUserId())
                .firstName(driver.getFirstName())
                .lastName(driver.getLastName())
                .birhDate(driver.getBirthDate())
                .build();
    }
}
