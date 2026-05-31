package com.example.WorkforceIQ.entity;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Department {
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int Dept_id;

	    @Column(nullable = false, unique = true)
	    private String departmentName;

	    @JsonProperty("slots")
	    @Column(nullable = false)
	    private int slots;

	    @JsonManagedReference
	    @OneToMany(mappedBy = "department",
	            cascade = CascadeType.ALL)
	    private List<Employee> employees = new ArrayList<>();

	    public Department() {}
	    public int getDept_id() {
	        return Dept_id;
	    }

	    
	    public void setDept_id(int dept_id) {
	        Dept_id = dept_id;
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

	    public List<Employee> getEmployees() {
	        return employees;
	    }

	    public void setEmployees(List<Employee> employees) {
	        this.employees = employees;
	    }
}
