package com.example.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Employee;
import com.example.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;
@Service
public class EmployeeService extends ServiceImpl<EmployeeMapper, Employee> {
}
