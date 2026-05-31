package com.example.WorkforceIQ.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.WorkforceIQ.Repository.EmployeeRepository;
import com.example.WorkforceIQ.entity.Employee;

@Service
public class AnalyticsService {

    @Autowired
    private EmployeeRepository employeeRepo;

    @Autowired
    private AIAnalysisService aiService;

    public Map<String, Object> getHiringStats(LocalDate startDate, LocalDate endDate) {

        List<Employee> allEmployees = employeeRepo.findAll();

        List<Employee> employees =
            employeeRepo.findByHireDateBetween(startDate, endDate);

        Map<String, Long> departmentCounts = employees.stream()
            .filter(e -> e.getDepartment() != null)
            .collect(Collectors.groupingBy(
                e -> e.getDepartment().getDepartmentName(),
                Collectors.counting()
            ));

        List<Map<String, Object>> departmentBreakdown = new ArrayList<>();
        for (Map.Entry<String, Long> entry : departmentCounts.entrySet()) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("department", entry.getKey());
            row.put("count", entry.getValue());
            departmentBreakdown.add(row);
        }

        departmentBreakdown.sort((a, b) ->
            Long.compare((Long) b.get("count"), (Long) a.get("count")));

        long maleHired = countByGender(employees, "Male");
        long femaleHired = countByGender(employees, "Female");
        long workforceMale = countByGender(allEmployees, "Male");
        long workforceFemale = countByGender(allEmployees, "Female");

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("startDate", startDate.toString());
        result.put("endDate", endDate.toString());
        result.put("totalHired", employees.size());
        result.put("maleCount", maleHired);
        result.put("femaleCount", femaleHired);
        result.put("workforceMale", workforceMale);
        result.put("workforceFemale", workforceFemale);
        result.put("workforceTotal", allEmployees.size());
        result.put("departmentBreakdown", departmentBreakdown);

        return result;
    }

    private long countByGender(List<Employee> employees, String gender) {
        return employees.stream()
            .filter(e -> e.getGender() != null
                    && e.getGender().trim().equalsIgnoreCase(gender))
            .count();
    }

    public Map<String, Object> getSalaryGapAnalysis(
            LocalDate startDate, LocalDate endDate) {

        List<Employee> employees =
            employeeRepo.findByHireDateBetween(startDate, endDate);

        if (employees.isEmpty()) {
            Map<String, Object> empty = new LinkedHashMap<>();
            empty.put("startDate", startDate.toString());
            empty.put("endDate", endDate.toString());
            empty.put("analysis", "No employees hired in this date range. Add employees to generate salary gap analysis.");
            empty.put("stats", Map.of());
            return empty;
        }

        Map<String, Object> stats = buildSalaryStats(employees);
        String prompt = buildSalaryGapPrompt(startDate, endDate, stats);
        String aiAnalysis = aiService.getAnalysis(prompt);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("startDate", startDate.toString());
        result.put("endDate", endDate.toString());
        result.put("stats", stats);
        result.put("analysis", aiAnalysis);

        return result;
    }

    private Map<String, Object> buildSalaryStats(List<Employee> employees) {

        Map<String, Object> stats = new LinkedHashMap<>();

        Map<String, Double> deptAvgSalary = employees.stream()
            .filter(e -> e.getDepartment() != null)
            .collect(Collectors.groupingBy(
                e -> e.getDepartment().getDepartmentName(),
                Collectors.averagingDouble(Employee::getSalary)
            ));
        stats.put("departmentAverageSalaries", deptAvgSalary);

        Map<String, Map<String, Double>> withinDept = new LinkedHashMap<>();
        Map<String, List<Employee>> byDept = employees.stream()
            .filter(e -> e.getDepartment() != null)
            .collect(Collectors.groupingBy(
                e -> e.getDepartment().getDepartmentName()
            ));

        for (Map.Entry<String, List<Employee>> entry : byDept.entrySet()) {
            List<Employee> deptEmployees = entry.getValue();
            double min = deptEmployees.stream()
                .mapToDouble(Employee::getSalary).min().orElse(0);
            double max = deptEmployees.stream()
                .mapToDouble(Employee::getSalary).max().orElse(0);
            double avg = deptEmployees.stream()
                .mapToDouble(Employee::getSalary).average().orElse(0);

            Map<String, Double> deptStats = new LinkedHashMap<>();
            deptStats.put("min", min);
            deptStats.put("max", max);
            deptStats.put("avg", avg);
            deptStats.put("gap", max - min);
            withinDept.put(entry.getKey(), deptStats);
        }
        stats.put("withinDepartmentGaps", withinDept);

        double maleAvg = employees.stream()
            .filter(e -> "Male".equalsIgnoreCase(e.getGender()))
            .mapToDouble(Employee::getSalary)
            .average().orElse(0);
        long maleCount = employees.stream()
            .filter(e -> "Male".equalsIgnoreCase(e.getGender()))
            .count();

        double femaleAvg = employees.stream()
            .filter(e -> "Female".equalsIgnoreCase(e.getGender()))
            .mapToDouble(Employee::getSalary)
            .average().orElse(0);
        long femaleCount = employees.stream()
            .filter(e -> "Female".equalsIgnoreCase(e.getGender()))
            .count();

        Map<String, Object> genderGap = new LinkedHashMap<>();
        genderGap.put("maleAverage", maleAvg);
        genderGap.put("maleCount", maleCount);
        genderGap.put("femaleAverage", femaleAvg);
        genderGap.put("femaleCount", femaleCount);
        genderGap.put("gap", maleAvg - femaleAvg);
        stats.put("genderPayGap", genderGap);

        return stats;
    }

    private String buildSalaryGapPrompt(
            LocalDate startDate,
            LocalDate endDate,
            Map<String, Object> stats) {

        @SuppressWarnings("unchecked")
        Map<String, Double> deptAvg =
            (Map<String, Double>) stats.get("departmentAverageSalaries");

        @SuppressWarnings("unchecked")
        Map<String, Map<String, Double>> withinDept =
            (Map<String, Map<String, Double>>) stats.get("withinDepartmentGaps");

        @SuppressWarnings("unchecked")
        Map<String, Object> genderGap =
            (Map<String, Object>) stats.get("genderPayGap");

        StringBuilder sb = new StringBuilder();
        sb.append("You are an HR analytics expert. Analyze salary gaps for employees hired between ")
          .append(startDate).append(" and ").append(endDate).append(".\n\n");

        sb.append("1. BETWEEN DEPARTMENTS (average salary per department):\n");
        for (Map.Entry<String, Double> e : deptAvg.entrySet()) {
            sb.append("   - ").append(e.getKey())
              .append(": INR ").append(String.format("%.0f", e.getValue())).append("\n");
        }

        sb.append("\n2. WITHIN DEPARTMENTS (min, max, avg, gap):\n");
        for (Map.Entry<String, Map<String, Double>> e : withinDept.entrySet()) {
            Map<String, Double> d = e.getValue();
            sb.append("   - ").append(e.getKey())
              .append(": min INR ").append(String.format("%.0f", d.get("min")))
              .append(", max INR ").append(String.format("%.0f", d.get("max")))
              .append(", avg INR ").append(String.format("%.0f", d.get("avg")))
              .append(", gap INR ").append(String.format("%.0f", d.get("gap")))
              .append("\n");
        }

        sb.append("\n3. GENDER PAY GAP:\n");
        sb.append("   - Male avg: INR ")
          .append(String.format("%.0f", (Double) genderGap.get("maleAverage")))
          .append(" (").append(genderGap.get("maleCount")).append(" employees)\n");
        sb.append("   - Female avg: INR ")
          .append(String.format("%.0f", (Double) genderGap.get("femaleAverage")))
          .append(" (").append(genderGap.get("femaleCount")).append(" employees)\n");
        sb.append("   - Gap: INR ")
          .append(String.format("%.0f", (Double) genderGap.get("gap"))).append("\n");

        sb.append("\nProvide a concise analysis with three sections:\n");
        sb.append("- Between Departments: compare department averages and highlight largest gaps\n");
        sb.append("- Within Departments: note internal salary spread and equity concerns\n");
        sb.append("- Gender Pay Gap: analyze male vs female salary difference\n");
        sb.append("Use INR or rupees (no $ symbol). Be actionable and professional. Keep under 200 words.");

        return sb.toString();
    }
}
