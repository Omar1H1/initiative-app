package com.Initiative.Initiative.app.model;

import com.Initiative.Initiative.app.enums.MatchStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.config.YamlProcessor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Match {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User demander;

    @ManyToOne
    private User receiver;

    private MatchStatus status;





}
