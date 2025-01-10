package com.Initiative.app.dto;

import com.Initiative.app.enums.RoleEnum;
import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@NonNull
@Setter
@Getter
public class PreRegister {


    private String firstName;
    private String lastName;
    private String email;
    private RoleEnum role;
}
