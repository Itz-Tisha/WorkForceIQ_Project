package com.example.WorkforceIQ.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.WorkforceIQ.entity.Department;
import com.example.WorkforceIQ.entity.Employee;
import com.example.WorkforceIQ.Repository.DepartmentRepository;
import com.example.WorkforceIQ.Repository.EmployeeRepository;
import com.example.WorkforceIQ.dto.EmployeeRequest;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository repo;
    
    @Autowired
    private DepartmentRepository departmentRepo;

    public Employee login(String email, String password) {

        Employee emp = repo.findByEmailAndPassword(email, password);

        return emp;
    }
    public Employee addEmployee(EmployeeRequest request) {

        Department dept =
            departmentRepo.findById(request.getDepartmentId())
            .orElse(null);

        Employee emp = new Employee();

        emp.setName(request.getName());
        emp.setEmail(request.getEmail());
        emp.setPassword(request.getPassword());
        emp.setGender(request.getGender());
        emp.setSalary(request.getSalary());
        emp.setRole(request.getRole());
        emp.setHireDate(LocalDate.now());
        emp.setYearsOfExperience(request.getYearsOfExperience());
        emp.setExperienceLastIncrementYear(LocalDate.now().getYear());

        emp.setDepartment(dept);

        return repo.save(emp);
    }
    
    public Employee updateEmployee(
            Long id,
            EmployeeRequest request) {

        Employee emp = repo.findById(id).orElse(null);

        if(emp == null) {
            return null;
        }

        Department dept =
                departmentRepo.findById(
                        request.getDepartmentId())
                .orElse(null);

        emp.setName(request.getName());
        emp.setEmail(request.getEmail());
        emp.setPassword(request.getPassword());
        emp.setGender(request.getGender());
        emp.setSalary(request.getSalary());
        emp.setRole(request.getRole());
        emp.setYearsOfExperience(request.getYearsOfExperience());
        emp.setDepartment(dept);

        return repo.save(emp);
    }

    public void deleteEmployee(Long id) {

        repo.deleteById(id);
    }
    public Employee getEmployeeById(Long id) {

        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    public List<Employee> getAllEmployees() {
        return repo.findAll();
    }
    
}