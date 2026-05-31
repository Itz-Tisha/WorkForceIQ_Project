
package com.example.WorkforceIQ.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.WorkforceIQ.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {

}