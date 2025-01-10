package com.Initiative.app.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PasswordRecoveryInfo {

    private String email;
}
