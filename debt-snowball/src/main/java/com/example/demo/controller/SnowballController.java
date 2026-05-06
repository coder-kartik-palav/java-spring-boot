package com.example.demo.controller;

import com.example.demo.dto.Debt;
import com.example.demo.dto.SnowballRequest;
import com.example.demo.dto.SnowballResponse;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.SnowballService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/calculator")
@CrossOrigin(origins = "*")
public class SnowballController {

    private final SnowballService snowballService;
    private final UserRepository userRepository;

    @Autowired
    public SnowballController(SnowballService snowballService, UserRepository userRepository) {
        this.snowballService = snowballService;
        this.userRepository = userRepository;
    }

    @GetMapping("/snowball")
    public ResponseEntity<SnowballResponse> calculateSnowball(Authentication auth) {
        try {
            User user = userRepository.findByUsername(auth.getName()).orElseThrow();
            
            SnowballRequest request = new SnowballRequest();
            request.setExtraMonthlyPayment(user.getExtraMonthlyPayment());
            request.setDebts(user.getDebts().stream()
                    .map(d -> new Debt(d.getName(), d.getBalance(), d.getInterestRate(), d.getMinimumPayment()))
                    .collect(Collectors.toList()));

            if(request.getDebts().isEmpty()) {
                // Return empty response if no debts
                return ResponseEntity.ok(new SnowballResponse(0, 0, java.util.Collections.emptyList()));
            }

            SnowballResponse response = snowballService.calculateSnowball(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
