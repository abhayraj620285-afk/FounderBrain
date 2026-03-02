package com.abhayraj.founderbrain.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class IndustryBenchmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String industry;
    private  Double averageGrowthRate;
    private  Double averageRunwayMonths;
}
