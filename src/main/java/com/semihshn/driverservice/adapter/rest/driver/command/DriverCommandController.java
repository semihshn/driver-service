package com.semihshn.driverservice.adapter.rest.driver.command;

import com.semihshn.driverservice.adapter.rest.driver.request.DriverCreateRequest;
import com.semihshn.driverservice.adapter.rest.driver.request.DriverUpdateRequest;
import com.semihshn.driverservice.adapter.rest.driver.response.DriverCreateResponse;
import com.semihshn.driverservice.adapter.rest.driver.response.DriverUpdateResponse;
import com.semihshn.driverservice.domain.driver.Driver;
import com.semihshn.driverservice.domain.driver.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/drivers")
public class DriverCommandController {
    private final DriverService driverService;

    @PostMapping()
    public ResponseEntity<DriverCreateResponse> create(@RequestBody @Valid DriverCreateRequest request) {
        Driver createdDriver = driverService.create(request.convertToDriver());

        DriverCreateResponse driverCreateResponse = DriverCreateResponse.from(createdDriver);

        return new ResponseEntity<>(driverCreateResponse, HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<DriverUpdateResponse> update(@RequestBody @Valid DriverUpdateRequest request) throws IOException {
        Driver updatedDriver = driverService.update(request.convertToDriver());

        DriverUpdateResponse driverUpdateResponse = DriverUpdateResponse.from(updatedDriver);

        return new ResponseEntity<>(driverUpdateResponse, HttpStatus.OK);
    }

    @DeleteMapping("{driverId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long driverId) {
        driverService.delete(driverId);
    }
}
