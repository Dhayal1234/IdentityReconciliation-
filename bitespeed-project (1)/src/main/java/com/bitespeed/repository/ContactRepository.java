package com.bitespeed.repository;

import com.bitespeed.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
    List<Contact> findByEmailOrPhoneNumber(String email, String phoneNumber);
}