package com.Initiative.app.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordRecoveryInfo {

    private String email;
}
