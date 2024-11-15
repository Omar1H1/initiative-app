 package com.Initiative.Initiative.app.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GeneratedColumn;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

 public class ContactForm {


   @Id
   @GeneratedValue
   private Long id;

   private String firstname;

   private String lastname;


   private String email;

   @Column(length = 250)
   private String project;


   private LocalDateTime contactRequestDate;

    @PrePersist
    protected void onCreate() {
        contactRequestDate = LocalDateTime.now();
    }

 }
