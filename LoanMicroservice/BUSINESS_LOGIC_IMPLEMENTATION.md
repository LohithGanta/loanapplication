# LoanMicroservice - Complete Business Logic Implementation

## 🎯 Overview
Port: 8084 | Database: loan_db | All Financial Calculations

## 📊 Business Logic Services

### 1. LoanEligibilityService.java
**Location:** `src/main/java/com/example/demo/service/LoanEligibilityService.java`

```java
package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public class LoanEligibilityService {

    // Calculate FOIR (Fixed Obligation to Income Ratio)
    public double calculateFOIR(double monthlyIncome, double existingEMI, double proposedEMI) {
        if (monthlyIncome == 0) return 100;
        return ((existingEMI + proposedEMI) / monthlyIncome) * 100;
    }

    // Calculate Net Disposable Income
    public double calculateNetIncome(double income, double expenses, double existingEMI) {
        return income - expenses - existingEMI;
    }

    // Calculate Emergency Fund Months
    public double calculateEmergencyFundMonths(double savings, double monthlyExpenses) {
        if (monthlyExpenses == 0) return 0;
        return savings / monthlyExpenses;
    }

    // Calculate Savings Ratio
    public double calculateSavingsRatio(double savings, double monthlyIncome) {
        if (monthlyIncome == 0) return 0;
        return (savings / monthlyIncome) * 100;
    }

    // Main Eligibility Decision
    public String checkEligibility(double foir, double emergencyFundMonths, boolean stableJob, double savingsRatio) {
        if (foir <= 40 && emergencyFundMonths >= 3 && stableJob) {
            return "APPROVED";
        } else if (foir > 40 && foir <= 50 && savingsRatio >= 10) {
            return "CONDITIONAL_APPROVAL";
        } else {
            return "REJECTED";
        }
    }

    // Get Risk Level
    public String getRiskLevel(double foir) {
        if (foir < 40) return "SAFE";
        if (foir <= 50) return "MODERATE";
        return "RISKY";
    }
}
```

### 2. EMICalculatorService.java
**Location:** `src/main/java/com/example/demo/service/EMICalculatorService.java`

```java
package com.example.demo.service;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class EMICalculatorService {

    // Calculate Monthly EMI
    public double calculateEMI(double principal, double annualRate, int months) {
        if (annualRate == 0) return principal / months;
        
        double monthlyRate = annualRate / (12 * 100);
        double emi = (principal * monthlyRate * Math.pow(1 + monthlyRate, months)) 
                     / (Math.pow(1 + monthlyRate, months) - 1);
        
        return Math.round(emi * 100.0) / 100.0;
    }

    // Calculate Total Interest
    public double calculateTotalInterest(double emi, int months, double principal) {
        return (emi * months) - principal;
    }

    // Calculate Total Repayment
    public double calculateTotalRepayment(double emi, int months) {
        return emi * months;
    }

    // Calculate Outstanding Balance
    public double calculateOutstandingBalance(double principal, double annualRate, int totalMonths, int paidMonths) {
        double monthlyRate = annualRate / (12 * 100);
        double emi = calculateEMI(principal, annualRate, totalMonths);
        
        double outstanding = principal * Math.pow(1 + monthlyRate, paidMonths) 
                           - emi * ((Math.pow(1 + monthlyRate, paidMonths) - 1) / monthlyRate);
        
        return Math.max(0, outstanding);
    }

    // Calculate Prepayment Impact
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
```

### 3. FinancialHealthScoreService.java
**Location:** `src/main/java/com/example/demo/service/FinancialHealthScoreService.java`

```java
package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public class FinancialHealthScoreService {

    // Calculate Financial Health Score (0-100)
    public int calculateScore(double savingsRatio, double foir, double emergencyFundMonths, boolean stableIncome) {
        int score = 0;

        // Savings Ratio (30 points)
        if (savingsRatio >= 20) {
            score += 30;
        } else {
            score += (int)(savingsRatio * 1.5);
        }

        // FOIR (30 points)
        if (foir <= 30) {
            score += 30;
        } else if (foir <= 40) {
            score += 25;
        } else if (foir <= 50) {
            score += 15;
        } else {
            score += 5;
        }

        // Emergency Fund (20 points)
        if (emergencyFundMonths >= 6) {
            score += 20;
        } else {
            score += (int)(emergencyFundMonths * 3.33);
        }

        // Income Stability (20 points)
        score += stableIncome ? 20 : 10;

        return Math.min(100, score);
    }

    // Get Score Classification
    public String getScoreClassification(int score) {
        if (score >= 80) return "EXCELLENT";
        if (score >= 60) return "STABLE";
        if (score >= 40) return "RISK";
        return "FINANCIALLY_WEAK";
    }

    // Get Recommendations based on score
    public String getRecommendations(int score, double savingsRatio, double foir, double emergencyFundMonths) {
        StringBuilder recommendations = new StringBuilder();

        if (score < 60) {
            recommendations.append("Focus on improving financial health. ");
        }

        if (savingsRatio < 20) {
            recommendations.append("Increase savings to at least 20% of income. ");
        }

        if (foir > 40) {
            recommendations.append("Reduce debt obligations. ");
        }

        if (emergencyFundMonths < 6) {
            recommendations.append("Build emergency fund to cover 6 months expenses. ");
        }

        if (recommendations.length() == 0) {
            return "Your financial health is excellent! Consider investment opportunities.";
        }

        return recommendations.toString();
    }
}
```

### 4. InvestmentAdvisorService.java
**Location:** `src/main/java/com/example/demo/service/InvestmentAdvisorService.java`

```java
package com.example.demo.service;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvestmentAdvisorService {

    // Get Investment Suggestions based on risk profile
    public List<String> getInvestmentSuggestions(double monthlyIncome, double monthlySavings, String riskProfile) {
        List<String> suggestions = new ArrayList<>();

        double surplusIncome = monthlySavings;

        if (surplusIncome < 2000) {
            suggestions.add("Build emergency fund first before investing");
            return suggestions;
        }

        switch (riskProfile.toUpperCase()) {
            case "LOW":
                suggestions.add("Fixed Deposits (FD) - 6-7% returns, safe");
                suggestions.add("Government Bonds - 7-8% returns, very safe");
                suggestions.add("PPF (Public Provident Fund) - 7.1% returns, tax-free");
                break;

            case "MEDIUM":
                suggestions.add("Balanced Mutual Funds - 10-12% returns");
                suggestions.add("Index Funds - 10-15% returns");
                suggestions.add("Corporate Bonds - 8-10% returns");
                suggestions.add("SIP in Equity Mutual Funds - ₹" + (int)(surplusIncome * 0.5) + "/month");
                break;

            case "HIGH":
                suggestions.add("Equity Mutual Funds - 12-18% returns");
                suggestions.add("Direct Stocks - High returns, high risk");
                suggestions.add("Sectoral Funds - 15-20% returns");
                suggestions.add("SIP in Aggressive Funds - ₹" + (int)(surplusIncome * 0.7) + "/month");
                break;

            default:
                suggestions.add("Start with low-risk investments");
        }

        return suggestions;
    }

    // Determine Risk Profile
    public String determineRiskProfile(int age, double monthlyIncome, double emergencyFundMonths, boolean stableJob) {
        if (age < 30 && stableJob && emergencyFundMonths >= 6) {
            return "HIGH";
        } else if (age < 45 && emergencyFundMonths >= 3) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }

    // Calculate SIP Returns
    public double calculateSIPReturns(double monthlyInvestment, double annualReturn, int years) {
        double monthlyRate = annualReturn / (12 * 100);
        int months = years * 12;

        double futureValue = monthlyInvestment * 
            ((Math.pow(1 + monthlyRate, months) - 1) / monthlyRate) * 
            (1 + monthlyRate);

        return Math.round(futureValue * 100.0) / 100.0;
    }
}
```

### 5. LoanRecommendationService.java
**Location:** `src/main/java/com/example/demo/service/LoanRecommendationService.java`

```java
package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public class LoanRecommendationService {

    // Recommend Loan Type
    public String recommendLoanType(double monthlyIncome, double netIncome, String purpose) {
        if (monthlyIncome > 100000 && netIncome > 50000) {
            return "HOME_LOAN - You qualify for home loan with good terms";
        } else if (monthlyIncome > 50000 && netIncome > 20000) {
            return "PERSONAL_LOAN - Suitable for personal needs, short tenure recommended";
        } else if (monthlyIncome > 30000) {
            return "SMALL_PERSONAL_LOAN - Limited amount, focus on building savings first";
        } else {
            return "NOT_RECOMMENDED - Focus on increasing income and savings";
        }
    }

    // Calculate Maximum Loan Amount
    public double calculateMaxLoanAmount(double monthlyIncome, double existingEMI, double interestRate, int tenureMonths) {
        double maxEMI = (monthlyIncome * 0.40) - existingEMI; // 40% FOIR limit
        
        if (maxEMI <= 0) return 0;

        double monthlyRate = interestRate / (12 * 100);
        double maxLoan = maxEMI * ((Math.pow(1 + monthlyRate, tenureMonths) - 1) / 
                         (monthlyRate * Math.pow(1 + monthlyRate, tenureMonths)));

        return Math.round(maxLoan * 100.0) / 100.0;
    }

    // Suggest Optimal Tenure
    public int suggestOptimalTenure(double loanAmount, double monthlyIncome, double interestRate) {
        double affordableEMI = monthlyIncome * 0.35; // 35% of income

        // Try different tenures
        for (int months = 12; months <= 360; months += 12) {
            double monthlyRate = interestRate / (12 * 100);
            double emi = (loanAmount * monthlyRate * Math.pow(1 + monthlyRate, months)) / 
                        (Math.pow(1 + monthlyRate, months) - 1);

            if (emi <= affordableEMI) {
                return months;
            }
        }

        return 360; // Max 30 years
    }
}
```

## 📝 Next: Create DTOs and Controllers

I'll create the DTOs and REST controllers in the next response to keep this organized.

**Database Setup:**
```sql
CREATE DATABASE loan_db;
```

**Ready to continue with DTOs and Controllers?**
