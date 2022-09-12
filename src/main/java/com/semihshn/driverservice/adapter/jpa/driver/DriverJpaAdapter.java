package com.semihshn.driverservice.adapter.jpa.driver;

import com.semihshn.driverservice.adapter.jpa.common.Status;
import com.semihshn.driverservice.domain.util.exception.ExceptionType;
import com.semihshn.driverservice.domain.util.exception.SemDataNotFoundException;
import com.semihshn.driverservice.domain.driver.Driver;
import com.semihshn.driverservice.domain.port.DriverPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverJpaAdapter implements DriverPort {
    private final DriverJpaRepository driverJpaRepository;

    @Override
    public Driver create(Driver driver) {
        return driverJpaRepository.save(DriverEntity.from(driver)).toModel();
    }

    @Override
    public void delete(Long driverId) {
        driverJpaRepository.findById(driverId)
                .ifPresent(user -> {
                    user.setStatus(Status.DELETED);
                    driverJpaRepository.save(user);
                });
    }

    @Override
    public Driver retrieve(Long id) {
        return retrieveDriverEntity(id)
                .toModel();
    }

    @Override
    public Driver retrieveByUserId(Long id) {
        return retrieveDriverEntityByUserId(id)
                .toModel();
    }

    private DriverEntity retrieveDriverEntity(Long id) {
        return driverJpaRepository.findById(id)
                .orElseThrow(() -> new SemDataNotFoundException(ExceptionType.DRIVER_DATA_NOT_FOUND));
    }

    private DriverEntity retrieveDriverEntityByUserId(Long id) {
        return driverJpaRepository.findByUserId(id)
                .orElseThrow(() -> new SemDataNotFoundException(ExceptionType.DRIVER_DATA_NOT_FOUND));
    }
}
