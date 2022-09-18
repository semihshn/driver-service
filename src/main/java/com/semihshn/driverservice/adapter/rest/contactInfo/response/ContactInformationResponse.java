package com.semihshn.driverservice.adapter.rest.contactInfo.response;

import com.semihshn.driverservice.domain.contactInfo.ContactInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactInformationResponse {
    private Long id;
    private Long driverId;
    private String type;
    private String address;

    public static ContactInformationResponse from(ContactInfo contact) {
        return ContactInformationResponse.builder()
                .id(contact.getId())
                .driverId(contact.getDriverId())
                .type(contact.getType())
                .address(contact.getAddress())
                .build();
    }
}
