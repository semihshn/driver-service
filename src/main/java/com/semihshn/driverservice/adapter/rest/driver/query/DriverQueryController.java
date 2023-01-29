package com.semihshn.driverservice.adapter.rest.driver.query;

import com.semihshn.driverservice.adapter.rest.driver.response.DriverResponse;
import com.semihshn.driverservice.domain.driver.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<DriverResponse> retrieve(@PathVariable Long driverId) {
        return new ResponseEntity<>(DriverResponse.from(driverService.retrieve(driverId)), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<DriverResponse>> retrieveAll() throws IOException {
        return new ResponseEntity<>(driverService.retrieveAll()
                .stream()
                .map(DriverResponse::from)
                .toList(), HttpStatus.OK);
    }

    @GetMapping("users/{userId}")
    public ResponseEntity<List<DriverResponse>> retrieveByUserId(@PathVariable Long userId) throws IOException {
        return new ResponseEntity<>(driverService.retrieveByUserId(userId)
                .stream()
                .map(DriverResponse::from)
                .toList(), HttpStatus.OK);
    }
}
