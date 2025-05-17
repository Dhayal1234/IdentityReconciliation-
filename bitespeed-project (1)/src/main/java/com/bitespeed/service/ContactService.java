package com.bitespeed.service;

import com.bitespeed.entity.Contact;
import com.bitespeed.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ContactService {
    @Autowired
    private ContactRepository repo;

    public Map<String, Object> identifyContact(String email, String phoneNumber) {
        List<Contact> matchedContacts = repo.findByEmailOrPhoneNumber(email, phoneNumber);
        if (matchedContacts.isEmpty()) {
            Contact newContact = Contact.builder()
                .email(email)
                .phoneNumber(phoneNumber)
                .linkPrecedence("primary")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
            newContact = repo.save(newContact);
            return buildResponse(newContact, List.of(), List.of(), List.of());
        }

        Contact primary = matchedContacts.stream()
                .filter(c -> "primary".equals(c.getLinkPrecedence()))
                .min(Comparator.comparing(Contact::getCreatedAt)).orElse(matchedContacts.get(0));

        Set<String> emails = new LinkedHashSet<>();
        Set<String> phones = new LinkedHashSet<>();
        List<Integer> secondaryIds = new ArrayList<>();

        for (Contact c : matchedContacts) {
            if (c.getId().equals(primary.getId())) {
                emails.add(c.getEmail());
                phones.add(c.getPhoneNumber());
            } else {
                emails.add(c.getEmail());
                phones.add(c.getPhoneNumber());
                secondaryIds.add(c.getId());
            }
        }

        boolean alreadyExists = matchedContacts.stream()
            .anyMatch(c -> Objects.equals(c.getEmail(), email) && Objects.equals(c.getPhoneNumber(), phoneNumber));

        if (!alreadyExists) {
            Contact secondary = Contact.builder()
                .email(email)
                .phoneNumber(phoneNumber)
                .linkedId(primary.getId())
                .linkPrecedence("secondary")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
            repo.save(secondary);
            secondaryIds.add(secondary.getId());
            emails.add(email);
            phones.add(phoneNumber);
        }

        return buildResponse(primary, new ArrayList<>(emails), new ArrayList<>(phones), secondaryIds);
    }

    private Map<String, Object> buildResponse(Contact primary, List<String> emails, List<String> phones, List<Integer> secondaryIds) {
        Map<String, Object> contactMap = new HashMap<>();
        contactMap.put("primaryContatctId", primary.getId());
        contactMap.put("emails", emails);
        contactMap.put("phoneNumbers", phones);
        contactMap.put("secondaryContactIds", secondaryIds);
        return Map.of("contact", contactMap);
    }
}