package com.example.demo.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "debts")
public class DebtItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double balance;
    private double interestRate;
    private double minimumPayment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    public DebtItem() {}

    public DebtItem(String name, double balance, double interestRate, double minimumPayment) {
        this.name = name;
        this.balance = balance;
        this.interestRate = interestRate;
        this.minimumPayment = minimumPayment;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }

    public double getMinimumPayment() { return minimumPayment; }
    public void setMinimumPayment(double minimumPayment) { this.minimumPayment = minimumPayment; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
