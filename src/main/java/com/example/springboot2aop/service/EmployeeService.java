package com.example.springboot2aop.service;

import com.example.springboot2aop.bean.Employee;
import com.example.springboot2aop.dao.EmployeeDao;
import com.example.springboot2aop.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeDao employeeDao;

    public List<Employee> getAllEmployees() {
        return employeeDao.findAll();
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeDao.findById(id);
    }

    public Employee createEmployee(Employee employee) {
        return employeeDao.save(employee);
    }

    public Employee updateEmployee(Long id, Employee employeeDetails) throws ResourceNotFoundException {
        employeeDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + id));
        employeeDetails.setId(id);

        final Employee updatedEmployee = employeeDao.save(employeeDetails);
        return updatedEmployee;
    }

    public Map<String, Boolean> deleteEmployee(Long id) throws ResourceNotFoundException {
        Employee employee = employeeDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + id));

        employeeDao.delete(employee);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }


}