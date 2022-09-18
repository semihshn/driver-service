package com.semihshn.driverservice.adapter.rest.contactInfo.query;

import com.semihshn.driverservice.adapter.rest.contactInfo.response.ContactInformationResponse;
import com.semihshn.driverservice.domain.contactInfo.ContactInfoService;
import lombok.RequiredArgsConstructor;
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
    public ContactInformationResponse retrieve(@PathVariable Long contactId) throws IOException {
        return ContactInformationResponse.from(contactInformationService.retrieve(contactId));
    }

    @GetMapping()
    public List<ContactInformationResponse> retrieveAll() throws IOException {
        return contactInformationService.retrieveAll()
                .stream()
                .map(contactInfo -> ContactInformationResponse.from(contactInfo))
                .collect(Collectors.toList());
    }

    @GetMapping("drivers/{driverId}")
    public List<ContactInformationResponse> retrieveByDriverId(@PathVariable Long driverId) throws IOException {
        return contactInformationService.retrieveByDriverId(driverId)
                .stream()
                .map(ContactInformationResponse::from)
                .toList();
    }
}
