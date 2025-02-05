package com.Initiative.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Message {

    @Id
    @GeneratedValue
    private Long id;

    private Long sender;

    private Long receiver;

    private String content;

    private Timestamp time;

    private boolean seen;


}
