package com.Initiative.app.dto;


import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterInfo {

    private String firstname;

    private String lastname;

    private String username;

    private String email;

    private String password;
}
