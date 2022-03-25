package com.semihshn.driverservice.adapter.jpa.contactInfo;

import com.semihshn.driverservice.adapter.jpa.common.Status;
import com.semihshn.driverservice.adapter.jpa.driver.DriverEntity;
import com.semihshn.driverservice.domain.contactInfo.ContactInfo;
import com.semihshn.driverservice.domain.exception.ExceptionType;
import com.semihshn.driverservice.domain.exception.SemDataNotFoundException;
import com.semihshn.driverservice.domain.driver.Driver;
import com.semihshn.driverservice.domain.port.ContactInfoPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactInfoJpaAdapter implements ContactInfoPort {
    private final ContactInfoJpaRepository contactInformationJpaRepository;

    @Override
    public ContactInfo create(ContactInfo contactInformation, Driver driver) {

        DriverEntity driverEntity= DriverEntity.from(driver);

        return contactInformationJpaRepository.save(ContactInfoEntity
                .from(contactInformation,driverEntity))
                .toModel();
    }

    @Override
    public void delete(Long id) {
        contactInformationJpaRepository.findById(id)
                .ifPresent(user -> {
                    user.setStatus(Status.DELETED);
                    contactInformationJpaRepository.save(user);
                });
    }

    @Override
    public ContactInfo retrieve(Long id) {
        return contactInformationJpaRepository.findById(id)
                .orElseThrow(() -> new SemDataNotFoundException(ExceptionType.CONTACT_INFO_DATA_NOT_FOUND))
                .toModel();
    }
}
