package com.abhayraj.founderbrain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FundingResponse {
    private String fundingSignal;
    private String reason;
    private int confidenceScore;
}
