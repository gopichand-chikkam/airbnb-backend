package com.gopi.airbnb.repository;

import com.gopi.airbnb.entitys.ContactInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactInfoRepo extends JpaRepository<ContactInfo,Long> {
    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);


}
