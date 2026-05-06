package com.example.demo.dto;

import java.util.List;

public class SnowballRequest {
    private double extraMonthlyPayment;
    private List<Debt> debts;

    public SnowballRequest() {}

    public SnowballRequest(double extraMonthlyPayment, List<Debt> debts) {
        this.extraMonthlyPayment = extraMonthlyPayment;
        this.debts = debts;
    }

    public double getExtraMonthlyPayment() {
        return extraMonthlyPayment;
    }

    public void setExtraMonthlyPayment(double extraMonthlyPayment) {
        this.extraMonthlyPayment = extraMonthlyPayment;
    }

    public List<Debt> getDebts() {
        return debts;
    }

    public void setDebts(List<Debt> debts) {
        this.debts = debts;
    }
}
