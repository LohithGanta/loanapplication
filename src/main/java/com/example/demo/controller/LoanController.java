package com.example.demo.controller;

import com.example.demo.dto.LoanRequest;
import com.example.demo.entity.Loan;
import com.example.demo.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<Loan> createLoan(@RequestBody LoanRequest request) {
        Loan created = loanService.createLoan(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
