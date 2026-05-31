package com.example.WorkforceIQ.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.WorkforceIQ.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    List<Department> findBySlotsGreaterThan(int slots);

    @Query("SELECT DISTINCT d FROM Department d LEFT JOIN FETCH d.employees")
    List<Department> findAllWithEmployees();
}