package com.semihshn.driverservice.adapter.jpa.contactInfo;

import com.semihshn.driverservice.adapter.jpa.common.Status;
import com.semihshn.driverservice.adapter.jpa.driver.DriverEntity;
import com.semihshn.driverservice.adapter.jpa.common.BaseEntity;
import com.semihshn.driverservice.domain.contactInfo.ContactInfo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity(name = "contact_informations")
@Table(name = "contact_informations")
public class ContactInfoEntity extends BaseEntity {

    private String type;
    private String address;

    @ManyToOne
    private DriverEntity driver;

    public static ContactInfoEntity from(ContactInfo contactInformation, DriverEntity driver) {
        ContactInfoEntity contactInformationEntity = new ContactInfoEntity();
        contactInformationEntity.type= contactInformation.getType();
        contactInformationEntity.address=contactInformation.getAddress();
        contactInformationEntity.driver =driver;
        contactInformationEntity.status= Status.ACTIVE;
        return contactInformationEntity;
    }

    public ContactInfo toModel() {
        return ContactInfo.builder()
                .id(id)
                .type(type)
                .address(address)
                .build();
    }
}
