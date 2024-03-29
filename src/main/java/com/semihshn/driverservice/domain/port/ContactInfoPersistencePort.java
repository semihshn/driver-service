package com.semihshn.driverservice.domain.port;

import com.semihshn.driverservice.domain.contactInfo.ContactInfo;
import com.semihshn.driverservice.domain.driver.Driver;

public interface ContactInfoPersistencePort {
    ContactInfo create(ContactInfo contactInformation, Driver driver);

    ContactInfo update(ContactInfo contactInfo, Driver driver);

    void delete(Long id);

    ContactInfo retrieve(Long id);
}
