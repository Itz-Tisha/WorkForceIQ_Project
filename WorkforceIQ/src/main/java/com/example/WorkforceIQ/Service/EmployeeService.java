package com.example.WorkforceIQ.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.WorkforceIQ.entity.Department;
import com.example.WorkforceIQ.entity.Employee;
import com.example.WorkforceIQ.entity.RemovedEmployee;
import com.example.WorkforceIQ.util.PasswordGenerator;
import com.example.WorkforceIQ.Repository.DepartmentRepository;
import com.example.WorkforceIQ.Repository.EmployeeRepository;
import com.example.WorkforceIQ.Repository.RemovedEmployeeRepository;
import com.example.WorkforceIQ.dto.EmployeeRequest;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository repo;

    @Autowired
    private DepartmentRepository departmentRepo;
    
    @Autowired
    private RemovedEmployeeRepository removedEmployeeRepo;
    
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

//    public Employee login(String email, String password) {
//        return repo.findByEmailAndPassword(email, password);
//    }

    public Employee login(String email, String password) {

        Employee emp = repo.findByEmail(email)
                .orElse(null);

        if (emp != null && passwordEncoder.matches(password, emp.getPassword())) {
            return emp;
        }

        return null;
    }
    
//    @Transactional
//    public Employee addEmployee(EmployeeRequest request) {
//
//        Department dept = departmentRepo.findById(request.getDepartmentId())
//                .orElseThrow(() -> new ResponseStatusException(
//                        HttpStatus.BAD_REQUEST, "Department not found"));
//
//        if (dept.getSlots() <= 0) {
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST, "No slots available in this department");
//        }
//
//        Employee emp = new Employee();
//        emp.setName(request.getName());
//        emp.setEmail(request.getEmail());
//        emp.setPassword(request.getPassword());
//        emp.setGender(request.getGender());
//        emp.setSalary(request.getSalary());
//        emp.setRole(request.getRole());
//        emp.setHireDate(LocalDate.now());
//        emp.setYearsOfExperience(request.getYearsOfExperience());
//        emp.setExperienceLastIncrementYear(LocalDate.now().getYear());
//        emp.setDepartment(dept);
//
//        dept.setSlots(dept.getSlots() - 1);
//        departmentRepo.save(dept);
//
//        return repo.save(emp);
//    }
    
    
    @Transactional
    public Employee addEmployee(EmployeeRequest request) {

        Department dept = departmentRepo.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Department not found"));

        if (dept.getSlots() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "No slots available in this department");
        }

        Employee emp = new Employee();

        emp.setName(request.getName());
        emp.setEmail(request.getEmail());

        // Generate random password
        String rawPassword = PasswordGenerator.generatePassword(10);

        // Convert password into BCrypt hash
        String hashedPassword = passwordEncoder.encode(rawPassword);

        // Store hashed password in DB
        emp.setPassword(hashedPassword);

        emp.setGender(request.getGender());
        emp.setSalary(request.getSalary());
        emp.setRole(request.getRole());
        emp.setHireDate(LocalDate.now());
        emp.setYearsOfExperience(request.getYearsOfExperience());
        emp.setExperienceLastIncrementYear(LocalDate.now().getYear());
        emp.setDepartment(dept);

        dept.setSlots(dept.getSlots() - 1);

        departmentRepo.save(dept);

        Employee savedEmployee = repo.save(emp);

        // Send generated password to employee email
        emailService.sendEmployeeCredentials(
                emp.getEmail(),
                emp.getName(),
                rawPassword
        );

        return savedEmployee;
    }

    @Transactional
    public Employee updateEmployee(Long id, EmployeeRequest request) {

        Employee emp = repo.findById(id).orElse(null);
        if (emp == null) {
            return null;
        }

        Department newDept = departmentRepo.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Department not found"));

        Department oldDept = emp.getDepartment();
        int oldDeptId = oldDept != null ? oldDept.getDept_id() : -1;

        if (newDept.getDept_id() != oldDeptId) {
            if (newDept.getSlots() <= 0) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "No slots available in the selected department");
            }
            newDept.setSlots(newDept.getSlots() - 1);
            departmentRepo.save(newDept);

            if (oldDept != null) {
                oldDept.setSlots(oldDept.getSlots() + 1);
                departmentRepo.save(oldDept);
            }
        }

        emp.setName(request.getName());
        emp.setEmail(request.getEmail());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            emp.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        emp.setGender(request.getGender());
        emp.setSalary(request.getSalary());
        emp.setRole(request.getRole());
        emp.setYearsOfExperience(request.getYearsOfExperience());
        emp.setDepartment(newDept);

        return repo.save(emp);
    }

    @Transactional
    public void deleteEmployee(Long id) {

        Employee emp = repo.findById(id).orElse(null);

        if (emp == null) {
            return;
        }

        // Save into RemovedEmployee table
        RemovedEmployee removed = new RemovedEmployee();

        removed.setName(emp.getName());
        removed.setEmail(emp.getEmail());
        removed.setGender(emp.getGender());
        removed.setSalary(emp.getSalary());
        removed.setRole(emp.getRole());
        removed.setHireDate(emp.getHireDate());
        removed.setYearsOfExperience(emp.getYearsOfExperience());
        removed.setExperienceLastIncrementYear(
                emp.getExperienceLastIncrementYear()
        );

        if (emp.getDepartment() != null) {
            removed.setDepartmentName(
                    emp.getDepartment().getDepartmentName()
            );
        }

        removed.setRemovedDate(LocalDate.now());

        removedEmployeeRepo.save(removed);

       
        Department dept = emp.getDepartment();

        if (dept != null) {
            dept.setSlots(dept.getSlots() + 1);
            departmentRepo.save(dept);
        }

      
        repo.deleteById(id);
    }

    public Employee getEmployeeById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    public List<Employee> getAllEmployees() {
        return repo.findAll();
    }
    
    public long getMonthlyRemovedEmployees() {
        return removedEmployeeRepo.countRemovedEmployeesThisMonth();
    }
}
