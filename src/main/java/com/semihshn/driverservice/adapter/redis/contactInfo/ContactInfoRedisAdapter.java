package com.semihshn.driverservice.adapter.redis.contactInfo;

import com.semihshn.driverservice.domain.contactInfo.ContactInfo;
import com.semihshn.driverservice.domain.port.ContactInfoCachePort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
public class ContactInfoRedisAdapter implements ContactInfoCachePort {

    private final RedisTemplate<String, ContactInfoCache> contactInfoRedisTemplate;

    public ContactInfoRedisAdapter(RedisTemplate<String, ContactInfoCache> contactInfoRedisTemplate) {
        this.contactInfoRedisTemplate = contactInfoRedisTemplate;
    }

    @Override
    public Optional<ContactInfo> retrieveContactInfo(Long contactInfoId) {
        ContactInfoCache contactInfoCache = contactInfoRedisTemplate.opsForValue().get("contactInfo:" + contactInfoId);

        return Optional.ofNullable(contactInfoCache)
                .map(ContactInfoCache::toModel);
    }

    @Override
    public void createContactInfo(ContactInfo contactInfo) {

        ContactInfoCache contactInfoCache = ContactInfoCache.from(contactInfo);
        contactInfoRedisTemplate.opsForValue().set("contactInfo:" + contactInfo.getId(), contactInfoCache, Duration.ofSeconds(30));

    }
}
