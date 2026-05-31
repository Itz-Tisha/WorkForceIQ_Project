package com.example.WorkforceIQ.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.WorkforceIQ.dto.DepartmentDTO;
import com.example.WorkforceIQ.entity.Department;
import com.example.WorkforceIQ.Repository.DepartmentRepository;

@Service
public class DepartmentService {

    private static final int DEFAULT_SLOTS = 10;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Transactional
    public Department addDepartment(Department department) {
        if (department.getSlots() <= 0) {
            department.setSlots(DEFAULT_SLOTS);
        }
        return departmentRepository.save(department);
    }

    @Transactional
    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepository.findAllWithEmployees().stream()
                .map(this::ensureAndConvert)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<DepartmentDTO> getAvailableDepartments() {
        return departmentRepository.findAllWithEmployees().stream()
                .map(this::ensureAndConvert)
                .filter(dto -> dto.getSlots() > 0)
                .collect(Collectors.toList());
    }

    public void deleteDepartment(int id) {
        departmentRepository.deleteById(id);
    }

    @Transactional
    public Department updateDepartment(int id, Department newDept) {

        Department oldDept = departmentRepository.findById(id).orElse(null);

        if (oldDept != null) {
            oldDept.setDepartmentName(newDept.getDepartmentName());
            if (newDept.getSlots() > 0) {
                oldDept.setSlots(newDept.getSlots());
            } else if (oldDept.getSlots() <= 0) {
                oldDept.setSlots(DEFAULT_SLOTS);
            }
            return departmentRepository.save(oldDept);
        }

        return null;
    }

    public Department getDepartmentById(int id) {
        Department dept = departmentRepository.findById(id).orElse(null);
        if (dept != null) {
            ensureValidSlots(dept);
        }
        return dept;
    }

    private DepartmentDTO ensureAndConvert(Department dept) {
        ensureValidSlots(dept);
        return DepartmentDTO.from(dept);
    }

    private void ensureValidSlots(Department dept) {
        int employeeCount = dept.getEmployees() != null ? dept.getEmployees().size() : 0;

        if (dept.getSlots() <= 0) {
            if (employeeCount == 0) {
                dept.setSlots(DEFAULT_SLOTS);
            } else {
                dept.setSlots(Math.max(1, DEFAULT_SLOTS - employeeCount));
            }
            departmentRepository.save(dept);
        }
    }
}
