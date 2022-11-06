package com.semihshn.driverservice.adapter.jpa.contactInfo;

import com.semihshn.driverservice.adapter.jpa.common.Status;
import com.semihshn.driverservice.adapter.jpa.driver.DriverEntity;
import com.semihshn.driverservice.domain.contactInfo.ContactInfo;
import com.semihshn.driverservice.domain.driver.Driver;
import com.semihshn.driverservice.domain.port.ContactInfoPersistencePort;
import com.semihshn.driverservice.domain.util.exception.ExceptionType;
import com.semihshn.driverservice.domain.util.exception.SemDataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ContactInfoJpaAdapter implements ContactInfoPersistencePort {
    private final ContactInfoJpaRepository contactInformationJpaRepository;

    @Override
    public ContactInfo create(ContactInfo contactInformation, Driver driver) {

        DriverEntity driverEntity = DriverEntity.from(driver);

        return contactInformationJpaRepository.save(ContactInfoEntity
                        .from(contactInformation, driverEntity))
                .toModel();
    }

    @Override
    public ContactInfo update(ContactInfo contactInfo, Driver driver) {

        DriverEntity driverEntity = DriverEntity.from(driver);

        ContactInfoEntity updateEntity = ContactInfoEntity.from(contactInfo, driverEntity);
        updateEntity.setCreatedDate(LocalDateTime.now());
        return contactInformationJpaRepository.save(updateEntity).toModel();
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
