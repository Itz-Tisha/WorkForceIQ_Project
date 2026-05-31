package com.example.WorkforceIQ.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.WorkforceIQ.entity.Department;
import com.example.WorkforceIQ.Repository.DepartmentRepository;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    // add department
    public Department addDepartment(Department department) {
        return departmentRepository.save(department);
    }

    // get all departments
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    // delete department
    public void deleteDepartment(int id) {
        departmentRepository.deleteById(id);
    }

    // update department
    public Department updateDepartment(int id, Department newDept) {

        Department oldDept =
                departmentRepository.findById(id).orElse(null);

        if(oldDept != null) {
            oldDept.setDepartmentName(newDept.getDepartmentName());
            return departmentRepository.save(oldDept);
        }

        return null;
    }

    // get single department
    public Department getDepartmentById(int id) {
        return departmentRepository.findById(id).orElse(null);
    }
}