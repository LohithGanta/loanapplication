package com.example.demo.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "loans")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String borrowerName;
    private String borrowerEmail;
    private BigDecimal loanAmount;
    private Integer tenureMonths;
    private Double interestRate;
    private String employmentType;

    // Result fields
    private String eligibilityStatus;
    private String riskLevel;
    private Double foir;
    private Double monthlyEMI;
    private Double totalInterest;
    private Double totalRepayment;
    private Integer financialHealthScore;
    private String scoreClassification;

    private LocalDateTime appliedAt;

    @PrePersist
    public void prePersist() {
        this.appliedAt = LocalDateTime.now();
    }

    public Loan() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBorrowerName() { return borrowerName; }
    public void setBorrowerName(String borrowerName) { this.borrowerName = borrowerName; }
    public String getBorrowerEmail() { return borrowerEmail; }
    public void setBorrowerEmail(String borrowerEmail) { this.borrowerEmail = borrowerEmail; }
    public BigDecimal getLoanAmount() { return loanAmount; }
    public void setLoanAmount(BigDecimal loanAmount) { this.loanAmount = loanAmount; }
    public Integer getTenureMonths() { return tenureMonths; }
    public void setTenureMonths(Integer tenureMonths) { this.tenureMonths = tenureMonths; }
    public Double getInterestRate() { return interestRate; }
    public void setInterestRate(Double interestRate) { this.interestRate = interestRate; }
    public String getEmploymentType() { return employmentType; }
    public void setEmploymentType(String employmentType) { this.employmentType = employmentType; }
    public String getEligibilityStatus() { return eligibilityStatus; }
    public void setEligibilityStatus(String eligibilityStatus) { this.eligibilityStatus = eligibilityStatus; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public Double getFoir() { return foir; }
    public void setFoir(Double foir) { this.foir = foir; }
    public Double getMonthlyEMI() { return monthlyEMI; }
    public void setMonthlyEMI(Double monthlyEMI) { this.monthlyEMI = monthlyEMI; }
    public Double getTotalInterest() { return totalInterest; }
    public void setTotalInterest(Double totalInterest) { this.totalInterest = totalInterest; }
    public Double getTotalRepayment() { return totalRepayment; }
    public void setTotalRepayment(Double totalRepayment) { this.totalRepayment = totalRepayment; }
    public Integer getFinancialHealthScore() { return financialHealthScore; }
    public void setFinancialHealthScore(Integer financialHealthScore) { this.financialHealthScore = financialHealthScore; }
    public String getScoreClassification() { return scoreClassification; }
    public void setScoreClassification(String scoreClassification) { this.scoreClassification = scoreClassification; }
    public LocalDateTime getAppliedAt() { return appliedAt; }
    public void setAppliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; }
}
