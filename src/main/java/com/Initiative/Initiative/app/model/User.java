package com.Initiative.Initiative.app.model;


import com.Initiative.Initiative.app.enums.RoleEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    private long id;

    @Column(unique = true)
    private String userName;


    private String firstName;

    private String lastName;

    private RoleEnum role;

    @Column(unique = true)
    private String email;

    private String password;

    private LocalDateTime registrationDate;

    private LocalDateTime activationDate;

}
