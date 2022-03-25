package com.semihshn.driverservice.domain.driver;

import com.semihshn.driverservice.domain.port.DriverPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverService {
    private final DriverPort driverPort;

    public Long create(Driver driver) {
        Driver temp = driverPort.create(driver);
        return temp.getId();
    }

    public Driver retrieve(Long id) {
        return driverPort.retrieve(id);
    }

    public Driver retrieveByUserId(Long id) {
        return driverPort.retrieveByUserId(id);
    }

    public void delete(Long driverId) {
        driverPort.delete(driverId);
    }
}
