package com.gopi.airbnb.Services;

import com.gopi.airbnb.entitys.ContactInfo;
import jakarta.validation.constraints.NotBlank;

public interface ContactInfoService {
    ContactInfo addContactDetails(@NotBlank(message = "ContactInfo is Required") ContactInfo contactInfo);

    boolean checkEmailExists(String email);

    boolean checkPhoneNumberExists(String phoneNumber);

    ContactInfo updateContactInfo(Long id, ContactInfo contactInfoRequest);

    ContactInfo updateContactInfoField(Long id, ContactInfo contactInfo);
}
