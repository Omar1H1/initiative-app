package com.Initiative.app.dto;


import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PasswordResetInfo {
    private String activationCode;
    private String password;
}
