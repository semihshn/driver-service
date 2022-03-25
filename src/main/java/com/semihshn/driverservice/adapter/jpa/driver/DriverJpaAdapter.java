package com.semihshn.driverservice.adapter.jpa.driver;

import com.semihshn.driverservice.adapter.jpa.common.Status;
import com.semihshn.driverservice.domain.exception.ExceptionType;
import com.semihshn.driverservice.domain.exception.SemDataNotFoundException;
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
    public void delete(Long id) {
        driverJpaRepository.findById(id)
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

    private DriverEntity retrieveDriverEntity(Long id) {
        return driverJpaRepository.findById(id)
                .orElseThrow(() -> new SemDataNotFoundException(ExceptionType.DRIVER_DATA_NOT_FOUND));
    }
}
