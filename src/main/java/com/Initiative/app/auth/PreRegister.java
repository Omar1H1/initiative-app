package com.Initiative.app.auth;

import com.Initiative.app.enums.RoleEnum;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@NonNull
public class PreRegister {


    private String firstName;
    private String lastName;
    private String email;
    private RoleEnum role;
}
