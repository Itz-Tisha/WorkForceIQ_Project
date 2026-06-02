package com.example.WorkforceIQ.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.WorkforceIQ.dto.PromotionRequest;
import com.example.WorkforceIQ.entity.Employee;
import com.example.WorkforceIQ.entity.Promotion;
import com.example.WorkforceIQ.Repository.EmployeeRepository;
import com.example.WorkforceIQ.Repository.PromotionRepository;

@Service
public class PromotionService {

    @Autowired
    private EmployeeRepository employeeRepo;

    @Autowired
    private PromotionRepository promotionRepo;

//    public List<Employee> getEligibleEmployees() {
//
//        return employeeRepo.findByYearsOfExperienceGreaterThanEqual(3);
//    }
    
    public List<Employee> getEligibleEmployees() {

        List<Employee> employees = employeeRepo.findAll();
        List<Employee> eligibleEmployees = new ArrayList<>();

        int currentYear = LocalDate.now().getYear();

        for (Employee emp : employees) {

            boolean experienceCheck =
                    emp.getYearsOfExperience() >= 3;

            boolean promotionCheck =
                    emp.getExperienceLastIncrementYear() == null ||
                    (currentYear - emp.getExperienceLastIncrementYear()) >= 2;
            
                    if(emp.getDepartment() == null){
                        continue;
                    }

            double deptAvgSalary =
                    employeeRepo.getDepartmentAverageSalary(
                            emp.getDepartment().getDept_id()
                    );

            boolean salaryCheck =
                    emp.getSalary() < deptAvgSalary;

            if (experienceCheck && promotionCheck && salaryCheck) {
                eligibleEmployees.add(emp);
            }
        }

        return eligibleEmployees;
    }

    public Employee promoteEmployee(Long employeeId, PromotionRequest request) {

        Employee emp = employeeRepo.findById(employeeId)
                .orElseThrow();

        Promotion promotion = new Promotion();

        promotion.setEmployee(emp);

        promotion.setOldRole(emp.getRole());
        promotion.setNewRole(request.getNewRole());

        promotion.setOldSalary(emp.getSalary());
        promotion.setNewSalary(request.getNewSalary());

        promotion.setPromotionDate(LocalDate.now());

        emp.setRole(request.getNewRole());
        emp.setSalary(request.getNewSalary());

        employeeRepo.save(emp);

        promotionRepo.save(promotion);

        return emp;
    }
}