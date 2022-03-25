package com.semihshn.driverservice.adapter.jpa.driver;

import com.semihshn.driverservice.adapter.jpa.common.BaseEntity;
import com.semihshn.driverservice.adapter.jpa.common.Status;
import com.semihshn.driverservice.adapter.jpa.contactInfo.ContactInfoEntity;
import com.semihshn.driverservice.domain.driver.Driver;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity(name = "drivers")
@Table(name = "drivers")
public class DriverEntity extends BaseEntity {

    private Long userId;
    private String firstName;
    private String lastName;
    private LocalDate birhDate;

    @OneToMany(mappedBy = "driver")
    private List<ContactInfoEntity> contactInformationEntities;

    public static DriverEntity from(Driver driver) {
        DriverEntity driverEntity = new DriverEntity();
        driverEntity.id = driver.getId();
        driverEntity.userId=driver.getUserId();
        driverEntity.firstName = driver.getFirstName();
        driverEntity.lastName = driver.getLastName();
        driverEntity.birhDate = driver.getBirhDate();
        driverEntity.status = Status.ACTIVE;
        return driverEntity;
    }

    public Driver toModel() {
        return Driver.builder()
                .id(id)
                .userId(userId)
                .firstName(firstName)
                .lastName(lastName)
                .birhDate(birhDate)
                .build();
    }
}
