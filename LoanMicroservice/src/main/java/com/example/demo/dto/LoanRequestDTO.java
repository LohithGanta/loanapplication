package com.example.demo.dto;

public class LoanRequestDTO {
    
    // User Financial Data
    private double monthlyIncome;
    private double monthlyExpenses;
    private double existingEMI;
    private double savings;
    private boolean stableJob;
    
    // Loan Details
    private double loanAmount;
    private int tenureMonths;
    private double interestRate;
    private String borrowerName;
    private String employmentType;

    public LoanRequestDTO() {}

    // Getters and Setters
    public double getMonthlyIncome() { return monthlyIncome; }
    public void setMonthlyIncome(double monthlyIncome) { this.monthlyIncome = monthlyIncome; }

    public double getMonthlyExpenses() { return monthlyExpenses; }
    public void setMonthlyExpenses(double monthlyExpenses) { this.monthlyExpenses = monthlyExpenses; }

    public double getExistingEMI() { return existingEMI; }
    public void setExistingEMI(double existingEMI) { this.existingEMI = existingEMI; }

    public double getSavings() { return savings; }
    public void setSavings(double savings) { this.savings = savings; }

    public boolean isStableJob() { return stableJob; }
    public void setStableJob(boolean stableJob) { this.stableJob = stableJob; }

    public double getLoanAmount() { return loanAmount; }
    public void setLoanAmount(double loanAmount) { this.loanAmount = loanAmount; }

    public int getTenureMonths() { return tenureMonths; }
    public void setTenureMonths(int tenureMonths) { this.tenureMonths = tenureMonths; }

    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }

    public String getBorrowerName() { return borrowerName; }
    public void setBorrowerName(String borrowerName) { this.borrowerName = borrowerName; }

    public String getEmploymentType() { return employmentType; }
    public void setEmploymentType(String employmentType) { this.employmentType = employmentType; }
}
