package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public class LoanEligibilityService {

    public double calculateFOIR(double monthlyIncome, double existingEMI, double proposedEMI) {
        if (monthlyIncome == 0) return 100;
        return ((existingEMI + proposedEMI) / monthlyIncome) * 100;
    }

    public double calculateNetIncome(double income, double expenses, double existingEMI) {
        return income - expenses - existingEMI;
    }

    public double calculateEmergencyFundMonths(double savings, double monthlyExpenses) {
        if (monthlyExpenses == 0) return 0;
        return savings / monthlyExpenses;
    }

    public double calculateSavingsRatio(double savings, double monthlyIncome) {
        if (monthlyIncome == 0) return 0;
        return (savings / monthlyIncome) * 100;
    }

    public String checkEligibility(double foir, double emergencyFundMonths, boolean stableJob, double savingsRatio) {
        if (foir <= 40 && emergencyFundMonths >= 3 && stableJob) {
            return "APPROVED";
        } else if (foir > 40 && foir <= 50 && savingsRatio >= 10) {
            return "CONDITIONAL_APPROVAL";
        } else {
            return "REJECTED";
        }
    }

    public String getRiskLevel(double foir) {
        if (foir < 40) return "SAFE";
        if (foir <= 50) return "MODERATE";
        return "RISKY";
    }
}
