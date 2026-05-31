package com.example.WorkforceIQ.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.WorkforceIQ.entity.Employee;
import com.example.WorkforceIQ.Service.EmployeeService;
import com.example.WorkforceIQ.dto.EmployeeRequest;

@RestController
@CrossOrigin(origins = "http://localhost:5174")
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/login")
    public Employee login(
            @RequestParam String email,
            @RequestParam String password) {

        return employeeService.login(email, password);
    }
    @PostMapping
    public Employee addEmployee(
            @RequestBody EmployeeRequest request) {
    	System.out.println("hi");
        System.out.println(request.getName());
        return employeeService.addEmployee(request);
    }
    
    @PutMapping("/{id}")
    public Employee updateEmployee(
            @PathVariable Long id,
            @RequestBody EmployeeRequest request) {

        return employeeService.updateEmployee(id, request);
    }

    
    
    @DeleteMapping("/{id}")
    public String deleteEmployee(@PathVariable Long id) {

        employeeService.deleteEmployee(id);

        return "Employee deleted successfully";
    }
    
    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    @GetMapping
    public java.util.List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

  
    
}