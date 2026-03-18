package com.example.demo.service;

import com.example.demo.dto.LoanRequest;
import com.example.demo.entity.Loan;
import com.example.demo.repository.LoanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoanService {

    private final LoanRepository loanRepository;

    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @Transactional
    public Loan createLoan(LoanRequest req) {
        Loan loan = new Loan(null, req.getAmount(), req.getTermMonths(), req.getBorrowerName());
        return loanRepository.save(loan);
    }
}
