package com.semihshn.driverservice.adapter.rest.contactInfo.command;

import com.semihshn.driverservice.adapter.rest.contactInfo.request.ContactInfoCreateRequest;
import com.semihshn.driverservice.adapter.rest.contactInfo.request.ContactInfoUpdateRequest;
import com.semihshn.driverservice.adapter.rest.contactInfo.response.ContactInfoCreateResponse;
import com.semihshn.driverservice.adapter.rest.contactInfo.response.ContactInfoUpdateResponse;
import com.semihshn.driverservice.domain.contactInfo.ContactInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contactinformations")
public class ContactInfoCommandController {

    private final ContactInfoService contactInformationService;

    @PostMapping()
    public ResponseEntity<ContactInfoCreateResponse> create(@RequestBody @Valid ContactInfoCreateRequest request) {
        Long createdContactId = contactInformationService.create(request.convertToContactInformation());
        ContactInfoCreateResponse contactInfoCreateResponse = ContactInfoCreateResponse.builder().id(createdContactId).build();
        return new ResponseEntity<>(contactInfoCreateResponse, HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<ContactInfoUpdateResponse> update(@RequestBody @Valid ContactInfoUpdateRequest request) throws IOException {
        Long updatedContactInfoId = contactInformationService.update(request.convertToContactInformation());
        ContactInfoUpdateResponse contactInfoUpdateResponse = ContactInfoUpdateResponse.builder().id(updatedContactInfoId).build();
        return new ResponseEntity<>(contactInfoUpdateResponse, HttpStatus.OK);
    }

    @DeleteMapping("{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId) {
        contactInformationService.delete(userId);
    }

}
