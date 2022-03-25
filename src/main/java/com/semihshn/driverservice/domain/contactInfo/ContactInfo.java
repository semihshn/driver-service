package com.semihshn.driverservice.domain.contactInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ContactInfo {

    private Long id;
    private Long driverId;
    private String type;
    private String address;
}
