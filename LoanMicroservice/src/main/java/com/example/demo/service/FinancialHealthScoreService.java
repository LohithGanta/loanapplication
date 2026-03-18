package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public class FinancialHealthScoreService {

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

    public String getScoreClassification(int score) {
        if (score >= 80) return "EXCELLENT";
        if (score >= 60) return "STABLE";
        if (score >= 40) return "RISK";
        return "FINANCIALLY_WEAK";
    }

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
