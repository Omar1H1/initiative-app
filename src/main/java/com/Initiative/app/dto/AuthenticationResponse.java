package com.Initiative.app.dto;


import com.Initiative.app.enums.RoleEnum;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthenticationResponse {

    private Long id;

    private String token;

    private String username;

    private String firstname;

    private String lastname;

    private RoleEnum role;
}
