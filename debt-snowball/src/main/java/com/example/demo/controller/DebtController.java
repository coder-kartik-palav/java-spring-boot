package com.example.demo.controller;

import com.example.demo.entity.DebtItem;
import com.example.demo.entity.User;
import com.example.demo.repository.DebtItemRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/debts")
@CrossOrigin(origins = "*")
public class DebtController {

    @Autowired
    private DebtItemRepository debtRepository;

    @Autowired
    private UserRepository userRepository;

    private User getAuthenticatedUser(Authentication auth) {
        return userRepository.findByUsername(auth.getName()).orElseThrow();
    }

    @GetMapping
    public ResponseEntity<List<DebtItem>> getUserDebts(Authentication auth) {
        return ResponseEntity.ok(getAuthenticatedUser(auth).getDebts());
    }

    @PostMapping
    public ResponseEntity<DebtItem> addDebt(@RequestBody DebtItem debt, Authentication auth) {
        User user = getAuthenticatedUser(auth);
        debt.setUser(user);
        return ResponseEntity.ok(debtRepository.save(debt));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDebt(@PathVariable Long id, Authentication auth) {
        User user = getAuthenticatedUser(auth);
        DebtItem debt = debtRepository.findById(id).orElseThrow();
        if (debt.getUser().getId().equals(user.getId())) {
            debtRepository.delete(debt);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(403).build();
    }
}
