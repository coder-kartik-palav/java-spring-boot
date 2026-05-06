package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/payment")
    public ResponseEntity<Double> getExtraPayment(Authentication auth) {
        User user = userRepository.findByUsername(auth.getName()).orElseThrow();
        return ResponseEntity.ok(user.getExtraMonthlyPayment());
    }

    @PutMapping("/payment")
    public ResponseEntity<?> setExtraPayment(@RequestBody Map<String, Double> payload, Authentication auth) {
        User user = userRepository.findByUsername(auth.getName()).orElseThrow();
        user.setExtraMonthlyPayment(payload.getOrDefault("extraMonthlyPayment", 0.0));
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }
}
