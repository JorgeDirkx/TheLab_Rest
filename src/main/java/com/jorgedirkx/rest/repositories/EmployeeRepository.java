package com.jorgedirkx.rest.repositories;

import com.jorgedirkx.rest.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {


}
