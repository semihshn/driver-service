package com.semihshn.driverservice.adapter.rest.contactInfo;

import com.semihshn.driverservice.adapter.rest.contactInfo.request.ContactInfoCreateRequest;
import com.semihshn.driverservice.adapter.rest.contactInfo.response.ContactInfoCreateResponse;
import com.semihshn.driverservice.adapter.rest.contactInfo.response.ContactInformationResponse;
import com.semihshn.driverservice.domain.contactInfo.ContactInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contactinformations")
public class ContactInfoController {

    private final ContactInfoService contactInformationService;

    @PostMapping()
    public ContactInfoCreateResponse create(@RequestBody @Valid ContactInfoCreateRequest request) {
        Long createdContactId = contactInformationService.create(request.convertToContactInformation());
        return ContactInfoCreateResponse.builder().id(createdContactId).build();
    }

    @DeleteMapping("{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId) {
        contactInformationService.delete(userId);
    }

    @GetMapping("{contactId}")
    public ContactInformationResponse retrieve(@PathVariable Long contactId) {
        return ContactInformationResponse.from(contactInformationService.retrieve(contactId));
    }
}
