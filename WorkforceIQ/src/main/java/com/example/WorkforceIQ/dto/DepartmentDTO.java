package com.example.WorkforceIQ.dto;

import com.example.WorkforceIQ.entity.Department;

public class DepartmentDTO {

    private int dept_id;
    private String departmentName;
    private int slots;
    private int employeeCount;

    public DepartmentDTO() {
    }

    public static DepartmentDTO from(Department dept) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.dept_id = dept.getDept_id();
        dto.departmentName = dept.getDepartmentName();
        dto.slots = dept.getSlots();
        dto.employeeCount = dept.getEmployees() != null ? dept.getEmployees().size() : 0;
        return dto;
    }

    public int getDept_id() {
        return dept_id;
    }

    public void setDept_id(int dept_id) {
        this.dept_id = dept_id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public int getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(int employeeCount) {
        this.employeeCount = employeeCount;
    }
}
