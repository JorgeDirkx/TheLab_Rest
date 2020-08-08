package com.jorgedirkx.rest.controllers;


import com.jorgedirkx.rest.entities.Employee;
import com.jorgedirkx.rest.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class EmployeeController {

    private EmployeeRepository repository;

    @Autowired
    public void setRepository(EmployeeRepository repository){
        this.repository = repository;
    }

    // Aggregate root

    //give list of employees
    @GetMapping("/employees")
    List<Employee> all() {
        return repository.findAll();
    }

    @PostMapping("/employees")
    Employee newEmployee(@RequestBody Employee newEmployee) {
        return repository.save(newEmployee);
    }

    // Single item

    @GetMapping("/employees/{id}")
    Employee one(@PathVariable Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    //idempotente bewerking; bij post een niet idempotente bewerking
    //id tussen gekrulde haakjes=een long variable
    @PutMapping("/employees/{id}")
    Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
        //without Lambda;
        /*Optional<Employee> employeeOptional = repository.findById(id);
        if(employeeOptional.isPresent()){
            Employee employee = employeeOptional.get();
                employee.setName(newEmployee.getName());
                employee.setRole(newEmployee.getRole());
                return repository.save(employee);

            }
        else{
            newEmployee.setId(id);
            return repository.save(newEmployee);

        }*/

        return repository.findById(id)
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                    return repository.save(employee);
                })
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    return repository.save(newEmployee);
                });
    }

    @DeleteMapping("/employees/{id}")
    void deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
    }
}

