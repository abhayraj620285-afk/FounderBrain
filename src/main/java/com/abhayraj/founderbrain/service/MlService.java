package com.abhayraj.founderbrain.service;
import com.abhayraj.founderbrain.model.Startup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MlService {
    private final RestTemplate restTemplate = new RestTemplate();

    public String getRiskPrediction(Startup startup) {

        String url = "http://127.0.0.1:8000/predict";

        Map<String, Object> request = new HashMap<>();
        request.put("revenue", startup.getRevenue());
        request.put("lastMonthRevenue", startup.getLastMonthRevenue());
        request.put("monthlyExpenses", startup.getMonthlyExpenses());
        request.put("cashReserve", startup.getCashReserve());
        request.put("users", startup.getUsers());

        Map<String, String> response =
                restTemplate.postForObject(url, request, Map.class);

        return response.get("risk");
    }
}
