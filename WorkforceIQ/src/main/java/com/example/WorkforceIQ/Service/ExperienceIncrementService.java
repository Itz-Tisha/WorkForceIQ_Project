package com.example.WorkforceIQ.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.WorkforceIQ.Repository.EmployeeRepository;
import com.example.WorkforceIQ.entity.Employee;

@Service
public class ExperienceIncrementService {

    @Autowired
    private EmployeeRepository employeeRepo;

    @Scheduled(cron = "0 0 1 * * *")
    @Transactional
    public void incrementExperienceOnAnniversary() {

        LocalDate today = LocalDate.now();
        List<Employee> employees = employeeRepo.findAll();

        for (Employee emp : employees) {
            if (emp.getHireDate() == null) {
                continue;
            }

            LocalDate hireDate = emp.getHireDate();

            if (hireDate.getMonth() != today.getMonth()
                    || hireDate.getDayOfMonth() != today.getDayOfMonth()) {
                continue;
            }

            if (today.equals(hireDate)) {
                continue;
            }

            int lastIncrementYear = emp.getExperienceLastIncrementYear() != null
                    ? emp.getExperienceLastIncrementYear()
                    : hireDate.getYear();

            if (today.getYear() > lastIncrementYear) {
                emp.setYearsOfExperience(emp.getYearsOfExperience() + 1);
                emp.setExperienceLastIncrementYear(today.getYear());
                employeeRepo.save(emp);
            }
        }
    }
}
