package com.semihshn.driverservice.domain.contactInfo;

import com.semihshn.driverservice.domain.driver.Driver;
import com.semihshn.driverservice.domain.port.ContactInfoPort;
import com.semihshn.driverservice.domain.port.DriverPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactInfoService {

    private final ContactInfoPort contactInformationPort;
    private final DriverPort driverPort;

    public Long create(ContactInfo contactInformation) {
        Driver driver = driverPort.retrieve(contactInformation.getDriverId());
        ContactInfo temp=contactInformationPort.create(contactInformation, driver);
        return temp.getId();
    }

    public ContactInfo retrieve(Long id) {
        return contactInformationPort.retrieve(id);
    }

    public void delete(Long id) {
        contactInformationPort.delete(id);
    }
}
