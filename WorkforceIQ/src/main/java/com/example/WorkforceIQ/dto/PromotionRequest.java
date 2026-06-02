package com.example.WorkforceIQ.dto;

public class PromotionRequest {

    private String newRole;
    private double newSalary;

    public PromotionRequest() {
    }

    public String getNewRole() {
        return newRole;
    }

    public void setNewRole(String newRole) {
        this.newRole = newRole;
    }

    public double getNewSalary() {
        return newSalary;
    }

    public void setNewSalary(double newSalary) {
        this.newSalary = newSalary;
    }
}