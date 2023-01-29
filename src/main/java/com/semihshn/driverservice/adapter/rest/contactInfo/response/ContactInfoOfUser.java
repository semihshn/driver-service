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
public class ContactInfoOfUser {

    private Long id;
    private Long userId;
    private String type;
    private String address;

    public static ContactInfoOfUser from(ContactInfo contact, Long userId) {
        return ContactInfoOfUser.builder()
                .id(contact.getId())
                .userId(userId)
                .type(contact.getType())
                .address(contact.getAddress())
                .build();
    }
}
