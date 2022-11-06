package com.semihshn.driverservice.adapter.redis.driver;

import com.semihshn.driverservice.domain.driver.Driver;
import com.semihshn.driverservice.domain.port.DriverCachePort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
public class DriverRedisAdapter implements DriverCachePort {

    private final RedisTemplate<String, DriverCache> driverCacheRedisTemplate;

    public DriverRedisAdapter(RedisTemplate<String, DriverCache> driverCacheRedisTemplate) {
        this.driverCacheRedisTemplate = driverCacheRedisTemplate;
    }

    @Override
    public Optional<Driver> retrieveDriver(Long driverId) {
        DriverCache driverCache = driverCacheRedisTemplate.opsForValue().get("driver:" + driverId);

        return Optional.ofNullable(driverCache)
                .map(DriverCache::toModel);
    }

    @Override
    public void createDriver(Driver driver) {
        DriverCache driverCache = DriverCache.from(driver);
        driverCacheRedisTemplate.opsForValue().set("driver:" + driver.getId(), driverCache, Duration.ofSeconds(30));
    }
}
