package com.Initiative.app.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterInfo {

    private String firstname;

    private String lastname;

    private String username;

    private String email;

    private String password;
}
