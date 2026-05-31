package com.example.WorkforceIQ.dto;

public class DepartmentAnalysisDTO {

    private String departmentName;
    private long maleCount;
    private long femaleCount;
    private double avgSalary;

    public DepartmentAnalysisDTO() {}

    public DepartmentAnalysisDTO(
            String departmentName,
            long maleCount,
            long femaleCount,
            double avgSalary) {

        this.departmentName = departmentName;
        this.maleCount = maleCount;
        this.femaleCount = femaleCount;
        this.avgSalary = avgSalary;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public long getMaleCount() {
        return maleCount;
    }

    public void setMaleCount(long maleCount) {
        this.maleCount = maleCount;
    }

    public long getFemaleCount() {
        return femaleCount;
    }

    public void setFemaleCount(long femaleCount) {
        this.femaleCount = femaleCount;
    }

    public double getAvgSalary() {
        return avgSalary;
    }

    public void setAvgSalary(double avgSalary) {
        this.avgSalary = avgSalary;
    }
}