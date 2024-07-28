package com.example.EMS.Controller;

import com.example.EMS.Entity.Employee;
import com.example.EMS.Service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/employee")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @GetMapping("/fetchEmployee")
    public ResponseEntity<?> fetchEmployee() {
        return employeeService.get();
    }

    @PostMapping("/createEmployee")
    public ResponseEntity<?> createEmployee(@RequestBody Employee employee) {
        return employeeService.add(employee);
    }

    @GetMapping("/findEmployee/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable(value = "id") Long employeeId) {
        return employeeService.getById(employeeId);
    }

    @PutMapping("/updateEmployee/{id}")
    public ResponseEntity<?> updateEmployeeById(@PathVariable(value = "id") Long employeeId, @RequestBody Employee employeeDetails) {
        return employeeService.updateById(employeeId, employeeDetails);
    }


    @DeleteMapping("/deleteEmployee/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable(value = "id") Long Id) {
        return employeeService.deleteById(Id);
    }

    @GetMapping("/{employeeId}/department")
    public ResponseEntity<?> departmentDetails(@PathVariable(value = "employeeId") Long employeeId) {
        return employeeService.departmentOfEmployee(employeeId);
    }

    @GetMapping("/highest-salary")
    public ResponseEntity<?> getHighestSalaryHolders() {
        return employeeService.getHighestSalaryHolders();
    }

    @GetMapping("/second-highest-salary")
    public ResponseEntity<?> getSecondHighestSalary() {
        return employeeService.getSecondHighestSalary();
    }


}
