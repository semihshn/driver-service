package com.semihshn.driverservice.adapter.rest.driver.command;

import com.semihshn.driverservice.adapter.rest.driver.request.DriverCreateRequest;
import com.semihshn.driverservice.adapter.rest.driver.response.DriverCreateResponse;
import com.semihshn.driverservice.domain.driver.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/drivers")
public class DriverCommandController {
    private final DriverService driverService;

    @PostMapping()
    public ResponseEntity<DriverCreateResponse> create(@RequestBody @Valid DriverCreateRequest request) {
        Long createdDriverId = driverService.create(request.convertToDriver());
        DriverCreateResponse driverCreateResponse = DriverCreateResponse.builder().id(createdDriverId).build();
        return new ResponseEntity<>(driverCreateResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("{driverId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long driverId) {
        driverService.delete(driverId);
    }
}
