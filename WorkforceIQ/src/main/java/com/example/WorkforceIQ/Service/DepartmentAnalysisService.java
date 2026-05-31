package com.example.WorkforceIQ.Service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.WorkforceIQ.Repository.DepartmentRepository;
import com.example.WorkforceIQ.entity.Department;
import com.example.WorkforceIQ.entity.Employee;

@Service
public class DepartmentAnalysisService {

    @Autowired
    private DepartmentRepository departmentRepo;

    @Autowired
    private AIAnalysisService aiService;

    public List<Map<String, String>> analyzeDepartments() {

        List<Department> departments =
            departmentRepo.findAll();

        List<Map<String, String>> result =
            new ArrayList<>();

        for(Department dept : departments) {

            long male = dept.getEmployees()
                    .stream()
                    .filter(e ->
                        e.getGender().equalsIgnoreCase("Male"))
                    .count();

            long female = dept.getEmployees()
                    .stream()
                    .filter(e ->
                        e.getGender().equalsIgnoreCase("Female"))
                    .count();

            double avgSalary = dept.getEmployees()
                    .stream()
                    .mapToDouble(Employee::getSalary)
                    .average()
                    .orElse(0);

            String prompt =
                "Analyze this department:\n" +
                "Department Name: " + dept.getDepartmentName() + "\n" +
                "Male Employees: " + male + "\n" +
                "Female Employees: " + female + "\n" +
                "Average Salary: " + avgSalary + "\n\n" +
                "Do not use $ symbol. Use INR or rupees. " +
                "Give short workforce insight in 2 lines.";

            String aiResponse =
                aiService.getAnalysis(prompt);

            Map<String, String> map =
                new HashMap<>();

            map.put("department",
                    dept.getDepartmentName());

            map.put("analysis", aiResponse);

            result.add(map);
        }

        return result;
    }
}