package com.abhayraj.founderbrain.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Instant expireToken;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
