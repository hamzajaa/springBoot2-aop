package com.example.springboot2aop.runner;

import com.example.springboot2aop.bean.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SpringRestClient implements CommandLineRunner {

    private static final String GET_EMPLOYEES_ENDPOINT_URL = "http://localhost:8056/api/v1/employees/";
    private static final String GET_EMPLOYEE_ENDPOINT_URL = "http://localhost:8056/api/v1/employees/id/{id}";
    private static final String CREATE_EMPLOYEE_ENDPOINT_URL = "http://localhost:8056/api/v1/employees/";
    private static final String UPDATE_EMPLOYEE_ENDPOINT_URL = "http://localhost:8056/api/v1/employees/id/{id}";
    private static final String DELETE_EMPLOYEE_ENDPOINT_URL = "http://localhost:8056/api/v1/employees/id/{id}";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void run(String... args) throws Exception {

        // Step1: first create a new employee
        createEmployee();

        // Step 2: get new created employee from step1
        getEmployeeById();

        // Step3: get all employees
        getEmployees();

        // Step4: Update employee with id = 1
        updateEmployee();

        // Step5: Delete employee with id = 1
        deleteEmployee();

    }

    private void getEmployees() {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<String> result = restTemplate.exchange(GET_EMPLOYEES_ENDPOINT_URL, HttpMethod.GET, entity, String.class);

        System.out.println(result);
    }

    private void getEmployeeById() {

        Map<String, String> params = new HashMap<>();
        params.put("id", "1");

        Employee result = restTemplate.getForObject(GET_EMPLOYEE_ENDPOINT_URL, Employee.class, params);

        System.out.println(result);
    }

    private void createEmployee() {

        Employee newEmployee = new Employee("admin", "admin", "admin@gmail.com");

        Employee result = restTemplate.postForObject(CREATE_EMPLOYEE_ENDPOINT_URL, newEmployee, Employee.class);

        System.out.println(result);
    }

    private void updateEmployee() {

        Map<String, String> params = new HashMap<>();
        params.put("id", "1");
        Employee updatedEmployee = new Employee("admin123", "admin123", "admin123@gmail.com");

        restTemplate.put(UPDATE_EMPLOYEE_ENDPOINT_URL, updatedEmployee, params);
    }

    private void deleteEmployee() {
        Map<String, String> params = new HashMap<>();
        params.put("id", "1");
        restTemplate.delete(DELETE_EMPLOYEE_ENDPOINT_URL, params);
    }
}
