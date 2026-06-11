package com.example.WorkforceIQ.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.WorkforceIQ.entity.Employee;
import com.example.WorkforceIQ.Service.EmployeeService;
import com.example.WorkforceIQ.dto.EmployeeRequest;
import com.example.WorkforceIQ.dto.LoginResponse;
import com.example.WorkforceIQ.security.JwtService;
import com.example.WorkforceIQ.util.Roles;

@RestController
@CrossOrigin(origins = "http://localhost:5174")
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestParam String email,
            @RequestParam String password) {

        Employee employee = employeeService.login(email, password);
        if (employee == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(jwtService.buildLoginResponse(employee));
    }

    @GetMapping("/roles")
    public java.util.List<String> getRoles() {
        return Roles.ALL;
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
    
    @GetMapping("/monthly-fired")
    public Map<String, Object> getMonthlyFiredEmployees() {

        long total = employeeService.getMonthlyRemovedEmployees();

        Map<String, Object> response = new HashMap<>();
        response.put("totalFired", total);

        return response;
    }

  
    
}