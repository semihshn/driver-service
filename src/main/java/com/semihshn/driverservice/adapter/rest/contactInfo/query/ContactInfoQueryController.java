package com.semihshn.driverservice.adapter.rest.contactInfo.query;

import com.semihshn.driverservice.adapter.rest.contactInfo.response.ContactInfoOfDriver;
import com.semihshn.driverservice.adapter.rest.contactInfo.response.ContactInfoOfUser;
import com.semihshn.driverservice.domain.contactInfo.ContactInfoService;
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
@RequestMapping("/api/contactinformations")
public class ContactInfoQueryController {

    private final ContactInfoService contactInformationService;

    @GetMapping("{contactId}")
    public ResponseEntity<ContactInfoOfDriver> retrieve(@PathVariable Long contactId) throws IOException {
        return new ResponseEntity<>(ContactInfoOfDriver.from(contactInformationService.retrieve(contactId)), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<ContactInfoOfDriver>> retrieveAll() throws IOException {
        return new ResponseEntity<>(contactInformationService.retrieveAll()
                .stream()
                .map(ContactInfoOfDriver::from)
                .toList(), HttpStatus.OK);
    }

    @GetMapping("drivers/{driverId}")
    public ResponseEntity<List<ContactInfoOfDriver>> retrieveByDriverId(@PathVariable Long driverId) throws IOException {
        return new ResponseEntity<>(contactInformationService.retrieveByDriverId(driverId)
                .stream()
                .map(ContactInfoOfDriver::from)
                .toList(), HttpStatus.OK);
    }

    @GetMapping("users/{userId}")
    public ResponseEntity<List<ContactInfoOfUser>> retrieveByUserId(@PathVariable Long userId) {
        return new ResponseEntity<>(contactInformationService.retrieveByUserId(userId)
                .stream()
                .map(contactInfo -> ContactInfoOfUser.from(contactInfo, userId))
                .toList(), HttpStatus.OK);
    }
}
