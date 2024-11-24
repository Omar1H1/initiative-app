package com.Initiative.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Initiative.app.model.ContactForm;



@Repository
public interface ContactFormRepository extends JpaRepository<ContactForm, Long> {
  
}
