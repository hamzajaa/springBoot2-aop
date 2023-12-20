package com.example.springboot2aop.service;

import com.example.springboot2aop.aspect.CheckValidationEmployeeFields;
import com.example.springboot2aop.bean.Employee;
import com.example.springboot2aop.dao.EmployeeDao;
import com.example.springboot2aop.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class EmployeeService {

    @Autowired
    private EmployeeDao employeeDao;

    public List<Employee> getAllEmployees() {
        return employeeDao.findAll();
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeDao.findById(id);
    }

    @CheckValidationEmployeeFields
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

    public void test(Employee employee) {
        employeeDao.save(employee);
        log.info("Employee saved successfully");
    }


}
