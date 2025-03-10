package com.Initiative.app.model;
import com.Initiative.app.enums.RoleEnum;
import com.Initiative.app.enums.SectorsOfActivity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.SecureRandom;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Collection;
import java.util.List;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@Builder
@EqualsAndHashCode
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true)
    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    @Column(columnDefinition="TEXT")
    private String projectDescription;

    @Enumerated(EnumType.STRING)
    private SectorsOfActivity sectorOfActivity;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;


    @Column(unique = true)
    private String activationCode;
    
    @Setter(AccessLevel.NONE)
    private LocalDateTime activationCodeExpiryDate;
    
    private Boolean isActive;

    @OneToMany
    @JsonIgnore
    private List<Match> matchList;


    @Lob
    @JdbcTypeCode(Types.BINARY)
    private byte[] profileImage;





    @PrePersist
    public void autoGenerateActivationCode() {
        if (activationCode == null || activationCode.isEmpty()) {
            byte[] randomBytes = new byte[14];
            new SecureRandom().nextBytes(randomBytes);
            this.activationCode = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
            this.activationCodeExpiryDate = LocalDateTime.now().plusYears(3);
        }
    }
    



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return LocalDateTime.now().isBefore(this.activationCodeExpiryDate);
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
