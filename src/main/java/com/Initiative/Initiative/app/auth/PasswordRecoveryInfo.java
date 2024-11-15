package com.Initiative.Initiative.app.auth;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordRecoveryInfo {

    private String email;
}
