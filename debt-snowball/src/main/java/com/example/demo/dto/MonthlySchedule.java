package com.example.demo.dto;

import java.util.List;

public class MonthlySchedule {
    private int month;
    private List<DebtState> debtsRemaining;

    public MonthlySchedule() {}

    public MonthlySchedule(int month, List<DebtState> debtsRemaining) {
        this.month = month;
        this.debtsRemaining = debtsRemaining;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public List<DebtState> getDebtsRemaining() {
        return debtsRemaining;
    }

    public void setDebtsRemaining(List<DebtState> debtsRemaining) {
        this.debtsRemaining = debtsRemaining;
    }
}
