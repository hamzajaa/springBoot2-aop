package com.example.springboot2aop.dao;

import com.example.springboot2aop.bean.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeDao extends JpaRepository<Employee, Long> {
}
