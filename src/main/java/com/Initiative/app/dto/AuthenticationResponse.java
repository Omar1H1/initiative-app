package com.Initiative.app.dto;


import com.Initiative.app.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private Long id;

    private String token;

    private String username;

    private String firstname;

    private String lastname;

    private RoleEnum role;
}
