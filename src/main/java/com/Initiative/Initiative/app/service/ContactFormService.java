package com.Initiative.Initiative.app.service;


import java.util.List;

import com.Initiative.Initiative.app.model.ContactForm;

public interface ContactFormService {
  ContactForm createContact(ContactForm contactForm);

  List<ContactForm> getAllContactRequests();
} 
