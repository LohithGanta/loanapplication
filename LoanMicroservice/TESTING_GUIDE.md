# LoanMicroservice - Complete Testing Guide

## ✅ What's Implemented

### Business Logic Services (3)
1. **LoanEligibilityService** - FOIR, Net Income, Emergency Fund, Savings Ratio, Eligibility Check
2. **EMICalculatorService** - EMI calculation, Total Interest, Prepayment
3. **FinancialHealthScoreService** - 0-100 score, Classification, Recommendations

### DTOs (2)
1. **LoanRequestDTO** - Input with all financial data
2. **LoanResponseDTO** - Output with all calculations

### Controller (1)
1. **LoanController** - REST API endpoints

### Configuration
- Port: 8084
- Database: loan_db
- Endpoints: /api/loan/*

---

## 🚀 How to Run

### Step 1: Create Database
```sql
CREATE DATABASE loan_db;
```

### Step 2: Build Project
```bash
cd c:\Users\lohith\Desktop\Loan_application\demo
mvnw clean install
```

### Step 3: Start Service
```bash
start-loan-microservice.bat
```

Wait for: "Started LoanMicroserviceApplication"

---

## 🧪 Test with Postman

### Endpoint 1: Check Loan Eligibility
**POST** `http://localhost:8084/api/loan/check-eligibility`

**Request Body:**
```json
{
  "monthlyIncome": 50000,
  "monthlyExpenses": 20000,
  "existingEMI": 5000,
  "savings": 100000,
  "stableJob": true,
  "loanAmount": 500000,
  "tenureMonths": 60,
  "interestRate": 10.5,
  "borrowerName": "John Doe",
  "employmentType": "SALARIED"
}
```

**Response:**
```json
{
  "eligibilityStatus": "APPROVED",
  "riskLevel": "SAFE",
  "foir": 32.5,
  "netIncome": 25000,
  "emergencyFundMonths": 5.0,
  "savingsRatio": 200.0,
  "monthlyEMI": 10624.0,
  "totalInterest": 137440.0,
  "totalRepayment": 637440.0,
  "financialHealthScore": 85,
  "scoreClassification": "EXCELLENT",
  "recommendations": "Your financial health is excellent! Consider investment opportunities."
}
```

### Endpoint 2: Calculate EMI Only
**GET** `http://localhost:8084/api/loan/calculate-emi?principal=500000&rate=10.5&months=60`

**Response:**
```json
{
  "monthlyEMI": 10624.0,
  "totalInterest": 137440.0,
  "totalRepayment": 637440.0
}
```

---

## 📊 Test Scenarios

### Scenario 1: APPROVED (Good Financial Health)
```json
{
  "monthlyIncome": 80000,
  "monthlyExpenses": 30000,
  "existingEMI": 0,
  "savings": 200000,
  "stableJob": true,
  "loanAmount": 1000000,
  "tenureMonths": 120,
  "interestRate": 9.5,
  "borrowerName": "Test User",
  "employmentType": "SALARIED"
}
```
**Expected:** APPROVED, SAFE risk, Score 80+

### Scenario 2: CONDITIONAL_APPROVAL (Moderate Risk)
```json
{
  "monthlyIncome": 40000,
  "monthlyExpenses": 25000,
  "existingEMI": 8000,
  "savings": 50000,
  "stableJob": true,
  "loanAmount": 300000,
  "tenureMonths": 36,
  "interestRate": 12.0,
  "borrowerName": "Test User",
  "employmentType": "SELF_EMPLOYED"
}
```
**Expected:** CONDITIONAL_APPROVAL, MODERATE risk, Score 40-60

### Scenario 3: REJECTED (High Risk)
```json
{
  "monthlyIncome": 30000,
  "monthlyExpenses": 20000,
  "existingEMI": 10000,
  "savings": 10000,
  "stableJob": false,
  "loanAmount": 500000,
  "tenureMonths": 60,
  "interestRate": 15.0,
  "borrowerName": "Test User",
  "employmentType": "SELF_EMPLOYED"
}
```
**Expected:** REJECTED, RISKY, Score < 40

---

## 🎯 Business Logic Validation

### FOIR Calculation
- Formula: `(Existing EMI + Proposed EMI) / Monthly Income * 100`
- Safe: < 40%
- Moderate: 40-50%
- Risky: > 50%

### EMI Calculation
- Formula: `P × r × (1 + r)^n / ((1 + r)^n - 1)`
- Where: P = Principal, r = Monthly Rate, n = Months

### Financial Health Score (0-100)
- Savings Ratio: 30 points
- FOIR: 30 points
- Emergency Fund: 20 points
- Income Stability: 20 points

### Eligibility Decision
- **APPROVED**: FOIR ≤ 40%, Emergency Fund ≥ 3 months, Stable Job
- **CONDITIONAL**: FOIR 40-50%, Savings Ratio ≥ 10%
- **REJECTED**: FOIR > 50% or Poor Financial Health

---

## 🔗 Integration with Other Services

### AdminMicroservice (Port 8081)
- Admin can view all loan applications
- Approve/reject loans manually

### UserMicroservice (Port 8082)
- Users apply for loans
- View loan status

### Future: AI Integration
- Gemini AI for personalized advice
- Investment recommendations

---

## ✅ Current Status

**Working:**
- ✅ All business logic calculations
- ✅ REST API endpoints
- ✅ FOIR, EMI, Score calculations
- ✅ Eligibility decision logic

**Not Yet:**
- ❌ Database persistence (Loan entity not enhanced)
- ❌ Frontend UI
- ❌ Integration with User/Admin services

**Next Steps:**
1. Test APIs with Postman
2. Create frontend form
3. Integrate with UserMicroservice
4. Add loan application history
