package com.semihshn.driverservice.domain.port;

import com.semihshn.driverservice.domain.driver.Driver;

public interface DriverPersistencePort {
    Driver create(Driver driver);

    Driver retrieve(Long id);

    Driver retrieveByUserId(Long id);

    Driver update(Driver driver);

    void delete(Long id);
}
