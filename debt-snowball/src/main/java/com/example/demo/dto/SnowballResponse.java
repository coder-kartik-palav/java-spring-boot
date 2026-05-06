package com.example.demo.dto;

import java.util.List;

public class SnowballResponse {
    private int totalMonthsToPayoff;
    private double totalInterestPaid;
    private List<MonthlySchedule> schedule;

    public SnowballResponse() {}

    public SnowballResponse(int totalMonthsToPayoff, double totalInterestPaid, List<MonthlySchedule> schedule) {
        this.totalMonthsToPayoff = totalMonthsToPayoff;
        this.totalInterestPaid = totalInterestPaid;
        this.schedule = schedule;
    }

    public int getTotalMonthsToPayoff() {
        return totalMonthsToPayoff;
    }

    public void setTotalMonthsToPayoff(int totalMonthsToPayoff) {
        this.totalMonthsToPayoff = totalMonthsToPayoff;
    }

    public double getTotalInterestPaid() {
        return totalInterestPaid;
    }

    public void setTotalInterestPaid(double totalInterestPaid) {
        this.totalInterestPaid = totalInterestPaid;
    }

    public List<MonthlySchedule> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<MonthlySchedule> schedule) {
        this.schedule = schedule;
    }
}
