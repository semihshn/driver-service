package com.semihshn.driverservice.adapter.rest.driver;

import com.semihshn.driverservice.adapter.rest.driver.request.DriverCreateRequest;
import com.semihshn.driverservice.adapter.rest.driver.response.DriverCreateResponse;
import com.semihshn.driverservice.adapter.rest.driver.response.DriverResponse;
import com.semihshn.driverservice.domain.driver.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/drivers")
public class DriverController {
    private final DriverService driverService;

    @PostMapping()
    public DriverCreateResponse create(@RequestBody @Valid DriverCreateRequest request) {
        Long createdDriverId = driverService.create(request.convertToDriver());
        return DriverCreateResponse.builder().id(createdDriverId).build();
    }

    @DeleteMapping("{driverId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long driverId) {
        driverService.delete(driverId);
    }

    @GetMapping("{driverId}")
    public DriverResponse retrieve(@PathVariable Long driverId) {
        return DriverResponse.from(driverService.retrieve(driverId));
    }

    @GetMapping("users/{userId}")
    public DriverResponse retrieveByUserId(@PathVariable Long userId) {
        return DriverResponse.from(driverService.retrieveByUserId(userId));
    }
}
