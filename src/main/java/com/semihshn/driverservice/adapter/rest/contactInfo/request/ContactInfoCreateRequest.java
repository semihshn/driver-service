package com.semihshn.driverservice.adapter.rest.contactInfo.request;

import com.semihshn.driverservice.domain.contactInfo.ContactInfo;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ContactInfoCreateRequest {

    @NotNull
    private Long driverId;

    @NotBlank
    private String type;

    @NotBlank
    private String address;

    public ContactInfo convertToContactInformation() {
        return ContactInfo.builder()
                .type(type)
                .address(address)
                .driverId(driverId)
                .build();
    }

}
