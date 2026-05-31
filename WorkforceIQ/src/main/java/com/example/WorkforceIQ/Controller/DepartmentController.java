package com.example.WorkforceIQ.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.WorkforceIQ.dto.DepartmentDTO;
import com.example.WorkforceIQ.entity.Department;
import com.example.WorkforceIQ.Service.DepartmentService;

@RestController
@CrossOrigin(origins = "http://localhost:5174")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @PostMapping("/department")
    public DepartmentDTO addDepartment(@RequestBody Department department) {
        Department saved = departmentService.addDepartment(department);
        return DepartmentDTO.from(saved);
    }

    @GetMapping("/departments")
    public List<DepartmentDTO> getDepartments() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/departments/available")
    public List<DepartmentDTO> getAvailableDepartments() {
        return departmentService.getAvailableDepartments();
    }

    @DeleteMapping("/department/{id}")
    public String deleteDepartment(@PathVariable int id) {
        departmentService.deleteDepartment(id);
        return "Department Deleted";
    }

    @PutMapping("/department/{id}")
    public DepartmentDTO updateDepartment(
            @PathVariable int id,
            @RequestBody Department department) {

        Department updated = departmentService.updateDepartment(id, department);
        return updated != null ? DepartmentDTO.from(updated) : null;
    }

    @GetMapping("/department/{id}")
    public Department getDepartment(@PathVariable int id) {
        return departmentService.getDepartmentById(id);
    }
}
