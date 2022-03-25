package com.semihshn.driverservice.adapter.jpa.driver;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DriverJpaRepository extends JpaRepository<DriverEntity, Long> {
    Optional<DriverEntity> findByUserId(Long userId);
}
