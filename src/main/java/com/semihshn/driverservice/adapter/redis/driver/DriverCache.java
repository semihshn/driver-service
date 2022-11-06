package com.semihshn.driverservice.adapter.redis.driver;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.semihshn.driverservice.domain.driver.Driver;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverCache {

    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate birthDate;

    public static DriverCache from(Driver driver) {
        return DriverCache.builder()
                .id(driver.getId())
                .userId(driver.getUserId())
                .firstName(driver.getFirstName())
                .lastName(driver.getLastName())
                .birthDate(driver.getBirthDate())
                .build();
    }

    public Driver toModel() {
        return Driver.builder()
                .id(id)
                .userId(userId)
                .firstName(firstName)
                .lastName(lastName)
                .birthDate(birthDate)
                .build();
    }

    public static List<DriverCache> from(List<Driver> drivers) {
        return drivers.stream()
                .map(DriverCache::from)
                .toList();
    }
}
