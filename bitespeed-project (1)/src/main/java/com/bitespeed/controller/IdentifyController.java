package com.bitespeed.controller;

import com.bitespeed.service.ContactService;
import com.bitespeed.entity.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/identify")
public class IdentifyController {
    @Autowired
    private ContactService contactService;

    @PostMapping
    public Map<String, Object> identify(@RequestBody Map<String, String> request) {
        return contactService.identifyContact(request.get("email"), request.get("phoneNumber"));
    }
}