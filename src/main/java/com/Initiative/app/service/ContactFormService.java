package com.Initiative.app.service;


import java.util.List;

import com.Initiative.app.model.ContactForm;

public interface ContactFormService {
  ContactForm createContact(ContactForm contactForm);

  List<ContactForm> getAllContactRequests();
} 
