package com.example.demo.dto;

public class Debt {
    private String name;
    private double balance;
    private double interestRate;
    private double minimumPayment;

    public Debt() {}

    public Debt(String name, double balance, double interestRate, double minimumPayment) {
        this.name = name;
        this.balance = balance;
        this.interestRate = interestRate;
        this.minimumPayment = minimumPayment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public double getMinimumPayment() {
        return minimumPayment;
    }

    public void setMinimumPayment(double minimumPayment) {
        this.minimumPayment = minimumPayment;
    }
}
