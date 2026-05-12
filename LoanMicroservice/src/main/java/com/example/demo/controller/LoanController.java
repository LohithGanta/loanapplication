package com.example.demo.controller;

import com.example.demo.dto.LoanRequestDTO;
import com.example.demo.dto.LoanResponseDTO;
import com.example.demo.entity.Loan;
import com.example.demo.repository.LoanRepository;
import com.example.demo.service.EMICalculatorService;
import com.example.demo.service.FinancialHealthScoreService;
import com.example.demo.service.LoanEligibilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/loan")
@CrossOrigin(origins = "*")
public class LoanController {

    private final LoanEligibilityService eligibilityService;
    private final EMICalculatorService emiCalculatorService;
    private final FinancialHealthScoreService healthScoreService;
    private final LoanRepository loanRepository;

    public LoanController(LoanEligibilityService eligibilityService,
                         EMICalculatorService emiCalculatorService,
                         FinancialHealthScoreService healthScoreService,
                         LoanRepository loanRepository) {
        this.eligibilityService = eligibilityService;
        this.emiCalculatorService = emiCalculatorService;
        this.healthScoreService = healthScoreService;
        this.loanRepository = loanRepository;
    }

    @PostMapping("/check-eligibility")
    public ResponseEntity<LoanResponseDTO> checkLoanEligibility(@RequestBody LoanRequestDTO request) {

        LoanResponseDTO response = new LoanResponseDTO();

        double emi = emiCalculatorService.calculateEMI(request.getLoanAmount(), request.getInterestRate(), request.getTenureMonths());
        response.setMonthlyEMI(emi);

        double totalInterest = emiCalculatorService.calculateTotalInterest(emi, request.getTenureMonths(), request.getLoanAmount());
        response.setTotalInterest(totalInterest);
        response.setTotalRepayment(emi * request.getTenureMonths());

        double foir = eligibilityService.calculateFOIR(request.getMonthlyIncome(), request.getExistingEMI(), emi);
        response.setFoir(foir);

        double netIncome = eligibilityService.calculateNetIncome(request.getMonthlyIncome(), request.getMonthlyExpenses(), request.getExistingEMI());
        response.setNetIncome(netIncome);

        double emergencyFundMonths = eligibilityService.calculateEmergencyFundMonths(request.getSavings(), request.getMonthlyExpenses());
        response.setEmergencyFundMonths(emergencyFundMonths);

        double savingsRatio = eligibilityService.calculateSavingsRatio(request.getSavings(), request.getMonthlyIncome());
        response.setSavingsRatio(savingsRatio);

        String eligibility = eligibilityService.checkEligibility(foir, emergencyFundMonths, request.isStableJob(), savingsRatio);
        response.setEligibilityStatus(eligibility);

        String riskLevel = eligibilityService.getRiskLevel(foir);
        response.setRiskLevel(riskLevel);

        int score = healthScoreService.calculateScore(savingsRatio, foir, emergencyFundMonths, request.isStableJob());
        response.setFinancialHealthScore(score);

        String classification = healthScoreService.getScoreClassification(score);
        response.setScoreClassification(classification);

        String recommendations = healthScoreService.getRecommendations(score, savingsRatio, foir, emergencyFundMonths);
        response.setRecommendations(recommendations);

        // Save to database
        Loan loan = new Loan();
        loan.setBorrowerName(request.getBorrowerName());
        loan.setBorrowerEmail(request.getUserEmail());
        loan.setLoanAmount(BigDecimal.valueOf(request.getLoanAmount()));
        loan.setTenureMonths(request.getTenureMonths());
        loan.setInterestRate(request.getInterestRate());
        loan.setEmploymentType(request.getEmploymentType());
        loan.setEligibilityStatus(eligibility);
        loan.setRiskLevel(riskLevel);
        loan.setFoir(foir);
        loan.setMonthlyEMI(emi);
        loan.setTotalInterest(totalInterest);
        loan.setTotalRepayment(emi * request.getTenureMonths());
        loan.setFinancialHealthScore(score);
        loan.setScoreClassification(classification);
        loanRepository.save(loan);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/calculate-emi")
    public ResponseEntity<?> calculateEMI(
            @RequestParam double principal,
            @RequestParam double rate,
            @RequestParam int months) {
        double emi = emiCalculatorService.calculateEMI(principal, rate, months);
        double totalInterest = emiCalculatorService.calculateTotalInterest(emi, months, principal);
        double totalRepayment = emiCalculatorService.calculateTotalRepayment(emi, months);
        return ResponseEntity.ok(new java.util.HashMap<String, Object>() {{
            put("monthlyEMI", emi);
            put("totalInterest", totalInterest);
            put("totalRepayment", totalRepayment);
        }});
    }

    // Get all loan applications (for admin)
    @GetMapping("/all")
    public ResponseEntity<List<Loan>> getAllLoans() {
        return ResponseEntity.ok(loanRepository.findAll());
    }

    // Get loan history by user email
    @GetMapping("/history")
    public ResponseEntity<List<Loan>> getLoanHistory(@RequestParam String email) {
        return ResponseEntity.ok(loanRepository.findByBorrowerEmail(email));
    }
}


    @PostMapping("/check-eligibility")
    public ResponseEntity<LoanResponseDTO> checkLoanEligibility(@RequestBody LoanRequestDTO request) {
        
        LoanResponseDTO response = new LoanResponseDTO();

        // Calculate EMI
        double emi = emiCalculatorService.calculateEMI(
            request.getLoanAmount(), 
            request.getInterestRate(), 
            request.getTenureMonths()
        );
        response.setMonthlyEMI(emi);

        // Calculate Total Interest and Repayment
        double totalInterest = emiCalculatorService.calculateTotalInterest(
            emi, 
            request.getTenureMonths(), 
            request.getLoanAmount()
        );
        response.setTotalInterest(totalInterest);
        response.setTotalRepayment(emi * request.getTenureMonths());

        // Calculate FOIR
        double foir = eligibilityService.calculateFOIR(
            request.getMonthlyIncome(), 
            request.getExistingEMI(), 
            emi
        );
        response.setFoir(foir);

        // Calculate Net Income
        double netIncome = eligibilityService.calculateNetIncome(
            request.getMonthlyIncome(), 
            request.getMonthlyExpenses(), 
            request.getExistingEMI()
        );
        response.setNetIncome(netIncome);

        // Calculate Emergency Fund Months
        double emergencyFundMonths = eligibilityService.calculateEmergencyFundMonths(
            request.getSavings(), 
            request.getMonthlyExpenses()
        );
        response.setEmergencyFundMonths(emergencyFundMonths);

        // Calculate Savings Ratio
        double savingsRatio = eligibilityService.calculateSavingsRatio(
            request.getSavings(), 
            request.getMonthlyIncome()
        );
        response.setSavingsRatio(savingsRatio);

        // Check Eligibility
        String eligibility = eligibilityService.checkEligibility(
            foir, 
            emergencyFundMonths, 
            request.isStableJob(), 
            savingsRatio
        );
        response.setEligibilityStatus(eligibility);

        // Get Risk Level
        String riskLevel = eligibilityService.getRiskLevel(foir);
        response.setRiskLevel(riskLevel);

        // Calculate Financial Health Score
        int score = healthScoreService.calculateScore(
            savingsRatio, 
            foir, 
            emergencyFundMonths, 
            request.isStableJob()
        );
        response.setFinancialHealthScore(score);

        // Get Score Classification
        String classification = healthScoreService.getScoreClassification(score);
        response.setScoreClassification(classification);

        // Get Recommendations
        String recommendations = healthScoreService.getRecommendations(
            score, 
            savingsRatio, 
            foir, 
            emergencyFundMonths
        );
        response.setRecommendations(recommendations);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/calculate-emi")
    public ResponseEntity<?> calculateEMI(
            @RequestParam double principal,
            @RequestParam double rate,
            @RequestParam int months) {
        
        double emi = emiCalculatorService.calculateEMI(principal, rate, months);
        double totalInterest = emiCalculatorService.calculateTotalInterest(emi, months, principal);
        double totalRepayment = emiCalculatorService.calculateTotalRepayment(emi, months);

        return ResponseEntity.ok(new java.util.HashMap<String, Object>() {{
            put("monthlyEMI", emi);
            put("totalInterest", totalInterest);
            put("totalRepayment", totalRepayment);
        }});
    }
}
