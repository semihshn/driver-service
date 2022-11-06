package com.semihshn.driverservice.domain.port;

import com.semihshn.driverservice.domain.contactInfo.ContactInfo;

import java.util.Optional;

public interface ContactInfoCachePort {

    Optional<ContactInfo> retrieveContactInfo(Long contactInfoId);

    void createContactInfo(ContactInfo contactInfo);

}
