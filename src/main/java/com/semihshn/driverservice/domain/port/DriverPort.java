package com.semihshn.driverservice.domain.port;

import com.semihshn.driverservice.domain.driver.Driver;

public interface DriverPort {
    Driver create(Driver driver);

    void delete(Long id);

    Driver retrieve(Long id);
}
