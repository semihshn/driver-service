package com.semihshn.driverservice.adapter.rest.contactInfo.query;

import com.semihshn.driverservice.adapter.rest.contactInfo.response.ContactInformationResponse;
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
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contactinformations")
public class ContactInfoQueryController {

    private final ContactInfoService contactInformationService;

    @GetMapping("{contactId}")
    public ResponseEntity<ContactInformationResponse> retrieve(@PathVariable Long contactId) throws IOException {
        return new ResponseEntity<>(ContactInformationResponse.from(contactInformationService.retrieve(contactId)), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<ContactInformationResponse>> retrieveAll() throws IOException {
        return new ResponseEntity<>(contactInformationService.retrieveAll()
                .stream()
                .map(contactInfo -> ContactInformationResponse.from(contactInfo))
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("drivers/{driverId}")
    public ResponseEntity<List<ContactInformationResponse>> retrieveByDriverId(@PathVariable Long driverId) throws IOException {
        return new ResponseEntity<>(contactInformationService.retrieveByDriverId(driverId)
                .stream()
                .map(ContactInformationResponse::from)
                .toList(), HttpStatus.OK);
    }
}
