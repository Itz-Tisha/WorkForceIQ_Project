package com.example.WorkforceIQ.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class RemovedEmployee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String gender;

    private double salary;

    private String role;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate hireDate;

    private int yearsOfExperience;

    private Integer experienceLastIncrementYear;

    private String departmentName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate removedDate;

    public RemovedEmployee() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public Integer getExperienceLastIncrementYear() {
        return experienceLastIncrementYear;
    }

    public void setExperienceLastIncrementYear(Integer experienceLastIncrementYear) {
        this.experienceLastIncrementYear = experienceLastIncrementYear;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public LocalDate getRemovedDate() {
        return removedDate;
    }

    public void setRemovedDate(LocalDate removedDate) {
        this.removedDate = removedDate;
    }
}