package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private double extraMonthlyPayment = 0.0;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DebtItem> debts = new ArrayList<>();

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public double getExtraMonthlyPayment() { return extraMonthlyPayment; }
    public void setExtraMonthlyPayment(double extraMonthlyPayment) { this.extraMonthlyPayment = extraMonthlyPayment; }

    public List<DebtItem> getDebts() { return debts; }
    public void setDebts(List<DebtItem> debts) { this.debts = debts; }

    public void addDebt(DebtItem debt) {
        debts.add(debt);
        debt.setUser(this);
    }

    public void removeDebt(DebtItem debt) {
        debts.remove(debt);
        debt.setUser(null);
    }
}
