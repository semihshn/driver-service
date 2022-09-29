package com.semihshn.driverservice.adapter.rest.driver.response;

import com.semihshn.driverservice.domain.driver.Driver;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverCreateResponse {
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;

    public static DriverCreateResponse from(Driver driver) {
        DriverCreateResponse driverCreateResponse = new DriverCreateResponse();
        driverCreateResponse.id = driver.getId();
        driverCreateResponse.userId = driver.getUserId();
        driverCreateResponse.firstName = driver.getFirstName();
        driverCreateResponse.lastName = driver.getLastName();
        driverCreateResponse.birthDate = driver.getBirthDate();
        return driverCreateResponse;
    }
}
