package com.semihshn.driverservice.domain.port;

import com.semihshn.driverservice.domain.driver.Driver;

import java.util.Optional;

public interface DriverCachePort {

    Optional<Driver> retrieveDriver(Long driverId);

    void createDriver(Driver driver);

}
