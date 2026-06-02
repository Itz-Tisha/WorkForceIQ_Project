package com.example.WorkforceIQ.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.WorkforceIQ.entity.Department;
import com.example.WorkforceIQ.entity.Employee;

public interface EmployeeRepository
        extends JpaRepository<Employee, Long> {

    Employee findByEmailAndPassword(String email, String password);

    @Query("SELECT e FROM Employee e WHERE e.hireDate BETWEEN :startDate AND :endDate")
    List<Employee> findByHireDateBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT AVG(e.salary) FROM Employee e")
    Double getCompanyAvgSalary();

    List<Employee> findByDepartment(Department department);
    Optional<Employee> findByEmail(String email);
    List<Employee> findByYearsOfExperienceGreaterThanEqual(int years);
    
    @Query("SELECT AVG(e.salary) FROM Employee e WHERE e.department.Dept_id = :deptId")
    double getDepartmentAverageSalary(@Param("deptId") int deptId);
}