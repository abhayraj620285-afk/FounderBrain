package com.abhayraj.founderbrain.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column
    private Long startupId;

    private String password;

    private String role;
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Startup> startups;

}
