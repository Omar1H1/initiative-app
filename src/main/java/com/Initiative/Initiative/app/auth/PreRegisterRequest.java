package com.Initiative.Initiative.app.auth;

import com.Initiative.Initiative.app.enums.RoleEnum;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@NonNull
public class PreRegisterRequest {


    private String firstName;
    private String lastName;
    private String email;
    private RoleEnum role;
}
