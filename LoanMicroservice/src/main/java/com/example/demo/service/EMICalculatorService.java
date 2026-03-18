package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public class EMICalculatorService {

    public double calculateEMI(double principal, double annualRate, int months) {
        if (annualRate == 0) return principal / months;
        
        double monthlyRate = annualRate / (12 * 100);
        double emi = (principal * monthlyRate * Math.pow(1 + monthlyRate, months)) 
                     / (Math.pow(1 + monthlyRate, months) - 1);
        
        return Math.round(emi * 100.0) / 100.0;
    }

    public double calculateTotalInterest(double emi, int months, double principal) {
        return (emi * months) - principal;
    }

    public double calculateTotalRepayment(double emi, int months) {
        return emi * months;
    }

    public double calculateOutstandingBalance(double principal, double annualRate, int totalMonths, int paidMonths) {
        double monthlyRate = annualRate / (12 * 100);
        double emi = calculateEMI(principal, annualRate, totalMonths);
        
        double outstanding = principal * Math.pow(1 + monthlyRate, paidMonths) 
                           - emi * ((Math.pow(1 + monthlyRate, paidMonths) - 1) / monthlyRate);
        
        return Math.max(0, outstanding);
    }

    public double calculatePrepaymentSavings(double principal, double annualRate, int remainingMonths, double prepayAmount) {
        double originalInterest = calculateTotalInterest(
            calculateEMI(principal, annualRate, remainingMonths), 
            remainingMonths, 
            principal
        );
        
        double newPrincipal = principal - prepayAmount;
        double newInterest = calculateTotalInterest(
            calculateEMI(newPrincipal, annualRate, remainingMonths), 
            remainingMonths, 
            newPrincipal
        );
        
        return originalInterest - newInterest;
    }
}
