package com.semihshn.driverservice.adapter.redis.contactInfo;

import com.semihshn.driverservice.domain.contactInfo.ContactInfo;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactInfoCache {

    private Long contactInfoId;
    private Long driverId;
    private String type;
    private String address;

    public static ContactInfoCache from(ContactInfo contactInfo) {
        return ContactInfoCache.builder()
                .contactInfoId(contactInfo.getId())
                .driverId(contactInfo.getDriverId())
                .address(contactInfo.getAddress())
                .type(contactInfo.getType())
                .build();
    }

    public ContactInfo toModel() {
        return ContactInfo.builder()
                .id(contactInfoId)
                .driverId(driverId)
                .type(type)
                .address(address)
                .build();
    }
}
