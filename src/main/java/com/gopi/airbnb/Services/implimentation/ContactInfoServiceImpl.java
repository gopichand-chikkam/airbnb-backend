package com.gopi.airbnb.Services.implimentation;

import com.gopi.airbnb.Services.ContactInfoService;
import com.gopi.airbnb.entitys.ContactInfo;
import com.gopi.airbnb.exceptions.ResourceNotFoundException;
import com.gopi.airbnb.repository.ContactInfoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactInfoServiceImpl implements ContactInfoService {
    private final ContactInfoRepo contactInfoRepo;

    @Override
    public ContactInfo addContactDetails(ContactInfo contactInfo) {
        return contactInfoRepo.save(contactInfo);
    }

    @Override
    public boolean checkEmailExists(String email) {
        return contactInfoRepo.existsByEmail(email);
    }

    @Override
    public boolean checkPhoneNumberExists(String phoneNumber) {
        return contactInfoRepo.existsByPhoneNumber(phoneNumber);
    }

    @Override
    public ContactInfo updateContactInfo(Long id, ContactInfo contactInfoRequest) {
        ContactInfo savedcontactInfo = contactInfoRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("contact_info not foud for id" + id));
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setId(savedcontactInfo.getId());
        contactInfo.setEmail(contactInfoRequest.getEmail());
        contactInfo.setLocation(contactInfoRequest.getLocation());
        contactInfo.setCompleteAddress(contactInfoRequest.getCompleteAddress());
        contactInfo.setPhoneNumber(contactInfoRequest.getPhoneNumber());
        return contactInfoRepo.save(contactInfo);

    }

    @Override
    public ContactInfo updateContactInfoField(Long id, ContactInfo contactInfo) {
        ContactInfo savedcontactInfo = contactInfoRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("contact_info not foud for id" + id));

        if (contactInfo.getLocation() != null) {
            savedcontactInfo.setLocation(contactInfo.getLocation());
        }
        if (contactInfo.getCompleteAddress() != null) {
            savedcontactInfo.setCompleteAddress(contactInfo.getCompleteAddress());
        }
        if (contactInfo.getEmail() != null) {
            savedcontactInfo.setEmail(contactInfo.getEmail());
        }
        if (contactInfo.getPhoneNumber() != null) {
            savedcontactInfo.setPhoneNumber(contactInfo.getPhoneNumber());
        }
        return contactInfoRepo.save(savedcontactInfo);
    }

}
