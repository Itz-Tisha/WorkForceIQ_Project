package com.example.WorkforceIQ.dto;

public class DepartmentHealthDTO {

    private String department;
    private int headcount;
    private int maleCount;
    private int femaleCount;
    private String genderRatio;
    private double avgSalary;
    private double minSalary;
    private double maxSalary;
    private long salaryHealthIndex;
    private String salaryHealthColor;
    private Long genderPayGapPercent;
    private boolean hasPayEquityIssue;
    private String payGapNote;
    private int slotsRemaining;

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getHeadcount() {
        return headcount;
    }

    public void setHeadcount(int headcount) {
        this.headcount = headcount;
    }

    public int getMaleCount() {
        return maleCount;
    }

    public void setMaleCount(int maleCount) {
        this.maleCount = maleCount;
    }

    public int getFemaleCount() {
        return femaleCount;
    }

    public void setFemaleCount(int femaleCount) {
        this.femaleCount = femaleCount;
    }

    public String getGenderRatio() {
        return genderRatio;
    }

    public void setGenderRatio(String genderRatio) {
        this.genderRatio = genderRatio;
    }

    public double getAvgSalary() {
        return avgSalary;
    }

    public void setAvgSalary(double avgSalary) {
        this.avgSalary = avgSalary;
    }

    public double getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(double minSalary) {
        this.minSalary = minSalary;
    }

    public double getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(double maxSalary) {
        this.maxSalary = maxSalary;
    }

    public long getSalaryHealthIndex() {
        return salaryHealthIndex;
    }

    public void setSalaryHealthIndex(long salaryHealthIndex) {
        this.salaryHealthIndex = salaryHealthIndex;
    }

    public String getSalaryHealthColor() {
        return salaryHealthColor;
    }

    public void setSalaryHealthColor(String salaryHealthColor) {
        this.salaryHealthColor = salaryHealthColor;
    }

    public Long getGenderPayGapPercent() {
        return genderPayGapPercent;
    }

    public void setGenderPayGapPercent(Long genderPayGapPercent) {
        this.genderPayGapPercent = genderPayGapPercent;
    }

    public boolean isHasPayEquityIssue() {
        return hasPayEquityIssue;
    }

    public void setHasPayEquityIssue(boolean hasPayEquityIssue) {
        this.hasPayEquityIssue = hasPayEquityIssue;
    }

    public String getPayGapNote() {
        return payGapNote;
    }

    public void setPayGapNote(String payGapNote) {
        this.payGapNote = payGapNote;
    }

    public int getSlotsRemaining() {
        return slotsRemaining;
    }

    public void setSlotsRemaining(int slotsRemaining) {
        this.slotsRemaining = slotsRemaining;
    }
}
