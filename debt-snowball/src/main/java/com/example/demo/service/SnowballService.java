package com.example.demo.service;

import com.example.demo.dto.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SnowballService {

    public SnowballResponse calculateSnowball(SnowballRequest request) {
        // Deep copy the debts so we don't modify the original request data
        List<Debt> currentDebts = request.getDebts().stream()
                .map(d -> new Debt(d.getName(), d.getBalance(), d.getInterestRate(), d.getMinimumPayment()))
                .collect(Collectors.toList());

        // The total amount of money paid every month is constant in the snowball method:
        // Extra monthly payment + sum of all initial minimum payments
        double totalBudget = request.getExtraMonthlyPayment() +
                currentDebts.stream().mapToDouble(Debt::getMinimumPayment).sum();

        int month = 0;
        double totalInterestPaid = 0;
        List<MonthlySchedule> schedule = new ArrayList<>();

        while (!currentDebts.isEmpty()) {
            month++;
            
            // 1. Apply monthly interest to all remaining debts
            for (Debt d : currentDebts) {
                double interest = d.getBalance() * (d.getInterestRate() / 100.0) / 12.0;
                d.setBalance(d.getBalance() + interest);
                totalInterestPaid += interest;
            }

            // 2. Sort debts by balance ascending (The core of the Snowball method)
            currentDebts.sort(Comparator.comparingDouble(Debt::getBalance));

            double remainingBudget = totalBudget;

            // 3. Pay minimums on all debts first
            for (Debt d : currentDebts) {
                double payment = Math.min(d.getMinimumPayment(), d.getBalance());
                d.setBalance(d.getBalance() - payment);
                remainingBudget -= payment;
            }

            // 4. Apply the remaining "snowball" budget to debts in order (smallest first)
            for (Debt d : currentDebts) {
                if (remainingBudget <= 0.001) break;
                if (d.getBalance() > 0) {
                    double payment = Math.min(remainingBudget, d.getBalance());
                    d.setBalance(d.getBalance() - payment);
                    remainingBudget -= payment;
                }
            }

            // 5. Record the state of debts for this month
            List<DebtState> states = currentDebts.stream()
                    .map(d -> new DebtState(d.getName(), round(Math.max(0, d.getBalance()))))
                    .collect(Collectors.toList());
            
            schedule.add(new MonthlySchedule(month, states));

            // 6. Remove debts that are fully paid off before the next month's loop
            currentDebts.removeIf(d -> d.getBalance() <= 0.001);

            // Safety break to prevent infinite loops (e.g. if payments don't cover interest)
            if (month > 1200) {
                throw new RuntimeException("Debts cannot be paid off with current payment plan. Interest exceeds payments.");
            }
        }

        return new SnowballResponse(month, round(totalInterestPaid), schedule);
    }

    // Helper method to format double to 2 decimal places for cleaner JSON output
    private double round(double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
