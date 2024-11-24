package com.Initiative.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.Initiative.app.model.ContactForm;
import com.Initiative.app.repository.ContactFormRepository;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class ContactFormImpl implements ContactFormService {
  
  private final ContactFormRepository repository; 

  @Override
  public ContactForm createContact(ContactForm contactForm) {

    return repository.save(contactForm);

  }

  @Override
  public List<ContactForm> getAllContactRequests() {
    return repository.findAll();
  }
}
