package com.abhayraj.founderbrain.controller;

import com.abhayraj.founderbrain.service.ChurnService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/churn")
@RequiredArgsConstructor
public class ChurnController {
    private final ChurnService churnService;
    @GetMapping("/{startupId}")
    public Map<String, Object> getChurn(@PathVariable Long startupId) {
        return churnService.calculateChurn(startupId);
    }
}
