package com.semihshn.driverservice.adapter.rest.driver.query;

import com.semihshn.driverservice.adapter.rest.driver.response.DriverResponse;
import com.semihshn.driverservice.domain.driver.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/drivers")
public class DriverQueryController {
    private final DriverService driverService;

    @GetMapping("{driverId}")
    public DriverResponse retrieve(@PathVariable Long driverId) throws IOException {
        return DriverResponse.from(driverService.retrieve(driverId));
    }

    @GetMapping()
    public List<DriverResponse> retrieveAll() throws IOException {
        return driverService.retrieveAll()
                .stream()
                .map(DriverResponse::from)
                .toList();
    }

    @GetMapping("users/{userId}")
    public List<DriverResponse> retrieveByUserId(@PathVariable Long userId) throws IOException {
        return driverService.retrieveByUserId(userId)
                .stream()
                .map(DriverResponse::from)
                .toList();
    }
}
