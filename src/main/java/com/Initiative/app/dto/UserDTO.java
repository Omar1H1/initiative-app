package com.Initiative.app.dto;

import com.Initiative.app.model.Match;
import com.Initiative.app.model.User;
import jakarta.persistence.Lob;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.List;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {

    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private Boolean isActive;


    private List<Match> matchList;


    public static UserDTO convertToDTO(User user) {

        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .isActive(user.getIsActive())
                .matchList(user.getMatchList())
                .build();

    }
}