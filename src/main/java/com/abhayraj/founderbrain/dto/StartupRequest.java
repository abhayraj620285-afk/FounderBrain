package com.abhayraj.founderbrain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StartupRequest {
    private String name;
    private String industry;
    private Double revenue;
    private Integer users;

}
