package com.abhayraj.founderbrain.repository;

import com.abhayraj.founderbrain.model.IndustryBenchmark;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IndustryBenchmarkRepository extends JpaRepository<IndustryBenchmark, Long> {
    Optional<IndustryBenchmark> findByIndustry(String industry);

}
