package com.example.demo.controller;

import com.example.demo.dto.LoanRequestDTO;
import com.example.demo.dto.LoanResponseDTO;
import com.example.demo.service.EMICalculatorService;
import com.example.demo.service.FinancialHealthScoreService;
import com.example.demo.service.LoanEligibilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loan")
@CrossOrigin(origins = "*")
public class LoanController {

    private final LoanEligibilityService eligibilityService;
    private final EMICalculatorService emiCalculatorService;
    private final FinancialHealthScoreService healthScoreService;

    public LoanController(LoanEligibilityService eligibilityService,
                         EMICalculatorService emiCalculatorService,
                         FinancialHealthScoreService healthScoreService) {
        this.eligibilityService = eligibilityService;
        this.emiCalculatorService = emiCalculatorService;
        this.healthScoreService = healthScoreService;
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
