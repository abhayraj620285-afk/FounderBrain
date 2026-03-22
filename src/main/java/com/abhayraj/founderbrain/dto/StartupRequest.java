package com.abhayraj.founderbrain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StartupRequest {

    @NotBlank(message = "Startup name cannot be empty")
    private String name;

    @NotBlank(message = "Industry cannot be empty")
    private String industry;

    @NotNull(message = "Revenue is required")
    @Positive(message = "Revenue must be positive")
    private Double revenue;

    @NotNull(message = "Users required")
    @PositiveOrZero
    private Integer users;

    @NotNull(message = "Last month revenue required")
    @PositiveOrZero
    private Double lastMonthRevenue;

    @NotNull(message = "Monthly expenses required")
    @Positive(message = "Expenses must be positive")
    private Double monthlyExpenses;

    @NotNull(message = "Cash reserve required")
    @Positive(message = "Cash must be positive")
    private Double cashReserve;

}
