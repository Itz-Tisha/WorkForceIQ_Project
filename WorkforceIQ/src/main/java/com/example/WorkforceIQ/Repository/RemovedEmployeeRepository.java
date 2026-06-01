package com.example.WorkforceIQ.Repository;

import com.example.WorkforceIQ.entity.RemovedEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RemovedEmployeeRepository extends JpaRepository<RemovedEmployee, Long> {
	 @Query("""
		        SELECT COUNT(r)
		        FROM RemovedEmployee r
		        WHERE MONTH(r.removedDate) = MONTH(CURRENT_DATE)
		        AND YEAR(r.removedDate) = YEAR(CURRENT_DATE)
		    """)
		    long countRemovedEmployeesThisMonth();
}