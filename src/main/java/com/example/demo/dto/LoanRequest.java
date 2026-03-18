package com.example.demo.dto;

import java.math.BigDecimal;

public class LoanRequest {
    private BigDecimal amount;
    private Integer termMonths;
    private String borrowerName;

    public LoanRequest() {}

    public LoanRequest(java.math.BigDecimal amount, Integer termMonths, String borrowerName) {
        this.amount = amount;
        this.termMonths = termMonths;
        this.borrowerName = borrowerName;
    }

    public java.math.BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(java.math.BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getTermMonths() {
        return termMonths;
    }

    public void setTermMonths(Integer termMonths) {
        this.termMonths = termMonths;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }
}
