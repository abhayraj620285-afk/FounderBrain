package com.abhayraj.founderbrain.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String actionType;

    private String featureName;

    private Integer sessionDuration;

    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "startup_id")
    private Startup startup;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
