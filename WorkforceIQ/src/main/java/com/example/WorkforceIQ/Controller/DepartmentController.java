package com.example.WorkforceIQ.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.WorkforceIQ.entity.Department;
import com.example.WorkforceIQ.Service.DepartmentService;

@RestController
@CrossOrigin(origins = "http://localhost:5174")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    // add department
    @PostMapping("/department")
    public Department addDepartment(
            @RequestBody Department department) {

        return departmentService.addDepartment(department);
    }

    // get all departments
    @GetMapping("/departments")
    public List<Department> getDepartments() {

        return departmentService.getAllDepartments();
    }

    // delete department
    @DeleteMapping("/department/{id}")
    public String deleteDepartment(@PathVariable int id) {

        departmentService.deleteDepartment(id);

        return "Department Deleted";
    }

    // update department
    @PutMapping("/department/{id}")
    public Department updateDepartment(
            @PathVariable int id,
            @RequestBody Department department) {

        return departmentService.updateDepartment(id, department);
    }

    // single department
    @GetMapping("/department/{id}")
    public Department getDepartment(@PathVariable int id) {

        return departmentService.getDepartmentById(id);
    }
}