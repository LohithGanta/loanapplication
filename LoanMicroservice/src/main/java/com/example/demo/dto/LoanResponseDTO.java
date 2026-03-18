package com.example.demo.dto;

public class LoanResponseDTO {
    
    // Eligibility Results
    private String eligibilityStatus;
    private String riskLevel;
    
    // Financial Metrics
    private double foir;
    private double netIncome;
    private double emergencyFundMonths;
    private double savingsRatio;
    
    // EMI Details
    private double monthlyEMI;
    private double totalInterest;
    private double totalRepayment;
    
    // Financial Health
    private int financialHealthScore;
    private String scoreClassification;
    private String recommendations;

    public LoanResponseDTO() {}

    // Getters and Setters
    public String getEligibilityStatus() { return eligibilityStatus; }
    public void setEligibilityStatus(String eligibilityStatus) { this.eligibilityStatus = eligibilityStatus; }

    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }

    public double getFoir() { return foir; }
    public void setFoir(double foir) { this.foir = foir; }

    public double getNetIncome() { return netIncome; }
    public void setNetIncome(double netIncome) { this.netIncome = netIncome; }

    public double getEmergencyFundMonths() { return emergencyFundMonths; }
    public void setEmergencyFundMonths(double emergencyFundMonths) { this.emergencyFundMonths = emergencyFundMonths; }

    public double getSavingsRatio() { return savingsRatio; }
    public void setSavingsRatio(double savingsRatio) { this.savingsRatio = savingsRatio; }

    public double getMonthlyEMI() { return monthlyEMI; }
    public void setMonthlyEMI(double monthlyEMI) { this.monthlyEMI = monthlyEMI; }

    public double getTotalInterest() { return totalInterest; }
    public void setTotalInterest(double totalInterest) { this.totalInterest = totalInterest; }

    public double getTotalRepayment() { return totalRepayment; }
    public void setTotalRepayment(double totalRepayment) { this.totalRepayment = totalRepayment; }

    public int getFinancialHealthScore() { return financialHealthScore; }
    public void setFinancialHealthScore(int financialHealthScore) { this.financialHealthScore = financialHealthScore; }

    public String getScoreClassification() { return scoreClassification; }
    public void setScoreClassification(String scoreClassification) { this.scoreClassification = scoreClassification; }

    public String getRecommendations() { return recommendations; }
    public void setRecommendations(String recommendations) { this.recommendations = recommendations; }
}
