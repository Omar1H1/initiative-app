package com.Initiative.app.dto;


import lombok.*;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PreRegisterCode {

    private String activationCode;
}
