package com.Initiative.app.model;

import com.Initiative.app.enums.MatchStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MatchStatus status = MatchStatus.pending;

    @Builder.Default
    private boolean isSeen = false;





}
