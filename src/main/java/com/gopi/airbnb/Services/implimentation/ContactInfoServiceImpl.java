package com.gopi.airbnb.Services.implimentation;

import com.gopi.airbnb.Services.ContactInfoService;
import com.gopi.airbnb.entitys.ContactInfo;
import com.gopi.airbnb.repository.ContactInfoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactInfoServiceImpl implements ContactInfoService{
    private final ContactInfoRepo contactInfoRepo;

    @Override
    public ContactInfo addContactDetails(ContactInfo contactInfo) {
         return contactInfoRepo.save(contactInfo);
    }

    @Override
    public boolean checkEmailExists(String email){
        return contactInfoRepo.existsByEmail(email);
    }

    @Override
    public boolean checkPhoneNumberExists(String phoneNumber){
        return contactInfoRepo.existsByPhoneNumber(phoneNumber);
    }

}
