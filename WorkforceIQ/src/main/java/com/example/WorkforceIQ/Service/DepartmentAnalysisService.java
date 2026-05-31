package com.example.WorkforceIQ.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.WorkforceIQ.Repository.DepartmentRepository;
import com.example.WorkforceIQ.Repository.EmployeeRepository;
import com.example.WorkforceIQ.dto.DepartmentHealthDTO;
import com.example.WorkforceIQ.entity.Department;
import com.example.WorkforceIQ.entity.Employee;

@Service
public class DepartmentAnalysisService {

    @Autowired
    private DepartmentRepository departmentRepo;

    @Autowired
    private EmployeeRepository employeeRepo;

    @Autowired
    private AIAnalysisService aiService;

    public Map<String, Object> analyzeDepartments() {

        List<Department> departments = departmentRepo.findAll();
        Double companyAvgSalaryRaw = employeeRepo.getCompanyAvgSalary();
        double companyAvgSalary = companyAvgSalaryRaw != null ? companyAvgSalaryRaw : 0;

        List<DepartmentHealthDTO> departmentResults = new ArrayList<>();
        List<DepartmentHealthDTO> healthDataForAi = new ArrayList<>();

        String lowestFemaleDeptName = null;
        long lowestFemaleCount = Long.MAX_VALUE;

        String lowestSalaryDeptName = null;
        double lowestAvgSalary = Double.MAX_VALUE;

        int departmentsWithEmployees = 0;

        for (Department dept : departments) {
            List<Employee> employees = employeeRepo.findByDepartment(dept);
            DepartmentHealthDTO metrics = buildDepartmentMetrics(dept, employees, companyAvgSalary);

            departmentResults.add(metrics);

            if (employees.isEmpty()) {
                continue;
            }

            departmentsWithEmployees++;
            healthDataForAi.add(metrics);

            if (metrics.getFemaleCount() < lowestFemaleCount) {
                lowestFemaleCount = metrics.getFemaleCount();
                lowestFemaleDeptName = dept.getDepartmentName();
            }

            if (metrics.getAvgSalary() < lowestAvgSalary) {
                lowestAvgSalary = metrics.getAvgSalary();
                lowestSalaryDeptName = dept.getDepartmentName();
            }
        }

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("companyAvgSalary", Math.round(companyAvgSalary));
        summary.put("totalDepartments", departments.size());
        summary.put("departmentsWithEmployees", departmentsWithEmployees);

        boolean canCompare = departmentsWithEmployees > 1;

        if (canCompare && lowestFemaleDeptName != null) {
            summary.put("lowestFemaleDepartment", lowestFemaleDeptName);
            summary.put("lowestFemaleCount", lowestFemaleCount);
        }

        if (canCompare && lowestSalaryDeptName != null) {
            summary.put("lowestAvgSalaryDepartment", lowestSalaryDeptName);
            summary.put("lowestAvgSalary", Math.round(lowestAvgSalary));
        }

        if (!healthDataForAi.isEmpty()) {
            summary.put("aiSummary", aiService.getAnalysis(
                    buildSummaryPrompt(companyAvgSalary, healthDataForAi, summary, canCompare)));
        } else {
            summary.put("aiSummary",
                    "No employees found across departments. Add employees to generate workforce analysis.");
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("summary", summary);
        result.put("departments", departmentResults);

        return result;
    }

    private DepartmentHealthDTO buildDepartmentMetrics(
            Department dept,
            List<Employee> employees,
            double companyAvgSalary) {

        DepartmentHealthDTO dto = new DepartmentHealthDTO();
        dto.setDepartment(dept.getDepartmentName());
        dto.setSlotsRemaining(dept.getSlots());

        long maleCount = countByGender(employees, "Male");
        long femaleCount = countByGender(employees, "Female");

        dto.setMaleCount((int) maleCount);
        dto.setFemaleCount((int) femaleCount);
        dto.setHeadcount(employees.size());
        dto.setGenderRatio(computeGenderRatio(maleCount, femaleCount));

        if (employees.isEmpty()) {
            dto.setAvgSalary(0);
            dto.setMinSalary(0);
            dto.setMaxSalary(0);
            dto.setSalaryHealthIndex(0);
            dto.setSalaryHealthColor("GREY");
            dto.setGenderPayGapPercent(null);
            dto.setHasPayEquityIssue(false);
            dto.setPayGapNote("No employees in this department");
            return dto;
        }

        double minSal = Double.MAX_VALUE;
        double maxSal = Double.MIN_VALUE;
        double totalSal = 0;

        for (Employee emp : employees) {
            double salary = emp.getSalary();
            totalSal += salary;
            if (salary < minSal) {
                minSal = salary;
            }
            if (salary > maxSal) {
                maxSal = salary;
            }
        }

        double deptAvgSalary = totalSal / employees.size();

        dto.setAvgSalary(deptAvgSalary);
        dto.setMinSalary(minSal);
        dto.setMaxSalary(maxSal);

        if (companyAvgSalary > 0) {
            double salaryHealthIndex = (deptAvgSalary / companyAvgSalary) * 100;
            dto.setSalaryHealthIndex(Math.round(salaryHealthIndex));
            dto.setSalaryHealthColor(resolveSalaryHealthColor(salaryHealthIndex));
        } else {
            dto.setSalaryHealthIndex(0);
            dto.setSalaryHealthColor("GREY");
        }

        applyGenderPayGapMetrics(dto, employees, maleCount, femaleCount);

        return dto;
    }

    private void applyGenderPayGapMetrics(
            DepartmentHealthDTO dto,
            List<Employee> employees,
            long maleCount,
            long femaleCount) {

        if (maleCount == 0 && femaleCount == 0) {
            dto.setGenderPayGapPercent(null);
            dto.setHasPayEquityIssue(false);
            dto.setPayGapNote("No gender data available");
            return;
        }

        if (maleCount == 0) {
            dto.setGenderPayGapPercent(null);
            dto.setHasPayEquityIssue(false);
            dto.setPayGapNote("No male employees — pay gap not applicable");
            return;
        }

        if (femaleCount == 0) {
            dto.setGenderPayGapPercent(null);
            dto.setHasPayEquityIssue(true);
            dto.setPayGapNote("No female employees — diversity and pay equity review recommended");
            return;
        }

        double maleAvgSalary = employees.stream()
                .filter(e -> isGender(e, "Male"))
                .mapToDouble(Employee::getSalary)
                .average()
                .orElse(0);

        double femaleAvgSalary = employees.stream()
                .filter(e -> isGender(e, "Female"))
                .mapToDouble(Employee::getSalary)
                .average()
                .orElse(0);

        if (femaleAvgSalary > 0) {
            double gapPercent = ((maleAvgSalary - femaleAvgSalary) / femaleAvgSalary) * 100;
            dto.setGenderPayGapPercent(Math.round(gapPercent));
            dto.setHasPayEquityIssue(Math.abs(gapPercent) > 10);
            dto.setPayGapNote(null);
        } else {
            dto.setGenderPayGapPercent(null);
            dto.setHasPayEquityIssue(false);
            dto.setPayGapNote("Female salary data unavailable");
        }
    }

    private String computeGenderRatio(long maleCount, long femaleCount) {
        if (maleCount == 0 && femaleCount == 0) {
            return "N/A";
        }
        if (femaleCount == 0) {
            return maleCount + ":0 (no female employees)";
        }
        if (maleCount == 0) {
            return "0:" + femaleCount + " (no male employees)";
        }
        return String.format("%.1f:1 (M:F)", (double) maleCount / femaleCount);
    }

    private String resolveSalaryHealthColor(double salaryHealthIndex) {
        if (salaryHealthIndex >= 90 && salaryHealthIndex <= 110) {
            return "GREEN";
        }
        if (salaryHealthIndex >= 75) {
            return "YELLOW";
        }
        return "RED";
    }

    private long countByGender(List<Employee> employees, String gender) {
        return employees.stream().filter(e -> isGender(e, gender)).count();
    }

    private boolean isGender(Employee employee, String gender) {
        return employee.getGender() != null
                && employee.getGender().trim().equalsIgnoreCase(gender);
    }

    private String buildSummaryPrompt(
            double companyAvgSalary,
            List<DepartmentHealthDTO> healthData,
            Map<String, Object> summary,
            boolean canCompare) {

        StringBuilder sb = new StringBuilder();
        sb.append("You are an HR analytics expert. Based on the following department health data, ")
          .append("write a concise executive summary (max 150 words) covering:\n")
          .append("- Overall workforce health across departments\n")
          .append("- Gender diversity concerns (especially departments with zero female employees)\n")
          .append("- Salary health vs company average and pay equity issues\n")
          .append("- Departments with low remaining hiring slots\n")
          .append("Use INR/rupees (no $). Be actionable and professional.\n\n");

        sb.append("Company average salary: INR ")
          .append(String.format("%.0f", companyAvgSalary)).append("\n\n");

        if (canCompare) {
            sb.append("Key flags:\n");
            if (summary.containsKey("lowestFemaleDepartment")) {
                sb.append("- Lowest female count: ")
                  .append(summary.get("lowestFemaleDepartment"))
                  .append(" (").append(summary.get("lowestFemaleCount")).append(" females)\n");
            }
            if (summary.containsKey("lowestAvgSalaryDepartment")) {
                sb.append("- Lowest avg salary: ")
                  .append(summary.get("lowestAvgSalaryDepartment"))
                  .append(" (INR ").append(summary.get("lowestAvgSalary")).append(")\n");
            }
            sb.append("\n");
        }

        sb.append("Department metrics:\n");
        for (DepartmentHealthDTO dept : healthData) {
            sb.append("- ").append(dept.getDepartment())
              .append(": headcount=").append(dept.getHeadcount())
              .append(", M/F=").append(dept.getMaleCount()).append("/").append(dept.getFemaleCount())
              .append(", ratio=").append(dept.getGenderRatio())
              .append(", avgSalary=INR ").append(String.format("%.0f", dept.getAvgSalary()))
              .append(", min=INR ").append(String.format("%.0f", dept.getMinSalary()))
              .append(", max=INR ").append(String.format("%.0f", dept.getMaxSalary()))
              .append(", salaryHealth=").append(dept.getSalaryHealthIndex()).append("% (")
              .append(dept.getSalaryHealthColor()).append(")")
              .append(", slotsLeft=").append(dept.getSlotsRemaining());

            if (dept.getGenderPayGapPercent() != null) {
                sb.append(", payGap=").append(dept.getGenderPayGapPercent()).append("%");
            }
            if (dept.getPayGapNote() != null) {
                sb.append(", note=").append(dept.getPayGapNote());
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
