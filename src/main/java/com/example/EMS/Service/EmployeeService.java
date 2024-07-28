package com.example.EMS.Service;


import com.example.EMS.Entity.Employee;
import com.example.EMS.Entity.Department;
import com.example.EMS.Repository.DepartmentRepository;
import com.example.EMS.Repository.EmployeeRepository;
import com.example.EMS.Response.DepartmentResponse;
import com.example.EMS.Response.EmployeeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    public ResponseEntity<?> get() {
        List<Employee> employees = employeeRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(employees);
    }

    public ResponseEntity<?> add(Employee employee) {
        Employee addedEmployee = employeeRepository.save(employee);
        EmployeeResponse employeeResponse = new EmployeeResponse(addedEmployee, HttpStatus.OK.value(), "", "Employee is Added");
        return ResponseEntity.status(HttpStatus.OK).body(employeeResponse);
    }

    public ResponseEntity<?> getById(Long employeeId) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            EmployeeResponse employeeResponse = new EmployeeResponse(employee, HttpStatus.OK.value(), "", "Employee with Id " + employeeId + " is Found");
//            System.out.println(employeeResponse);
            return ResponseEntity.status(HttpStatus.OK).body(employeeResponse);
        } else {
            EmployeeResponse employeeResponse = new EmployeeResponse(HttpStatus.NOT_FOUND.value(), "Not Found", "Employee with Id " + employeeId + " is not exists");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(employeeResponse);
        }
    }

    public ResponseEntity<?> updateById(Long employeeId, Employee employeeDetails) {
        Optional<Employee> optionalEmployee = employeeRepository.findById((employeeId));
        if (optionalEmployee.isPresent()) {
            Employee employee = getEmployee(employeeDetails, optionalEmployee);
            Employee updatedEmployee = employeeRepository.save(employee);
            EmployeeResponse employeeResponse = new EmployeeResponse(updatedEmployee, HttpStatus.OK.value(), "", "Employee with Id " + employeeId + " is updated");
            return ResponseEntity.status(HttpStatus.OK).body(employeeResponse);
        } else {
            EmployeeResponse employeeResponse = new EmployeeResponse(HttpStatus.NOT_FOUND.value(), "Not Found", "Employee with Id " + employeeId + " is not exists");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(employeeResponse);
        }
    }

    private static Employee getEmployee(Employee employeeDetails, Optional<Employee> optionalEmployee) {
        Employee employee = optionalEmployee.get();
        employee.setDepartment(employeeDetails.getDepartment() == null ? employee.getDepartment() : employeeDetails.getDepartment());
        employee.setName(employeeDetails.getName() == null ? employee.getName() : employeeDetails.getName());
        employee.setSalary(employeeDetails.getSalary() == 0 ? employee.getSalary() : employeeDetails.getSalary());
        employee.setAddress(employeeDetails.getAddress() == null ? employee.getAddress() : employeeDetails.getAddress());
        employee.setDesignation(employeeDetails.getDesignation() == null ? employee.getDesignation() : employeeDetails.getDesignation());
        employee.setProjects(employeeDetails.getProjects() == null ? employee.getProjects() : employeeDetails.getProjects());
        return employee;
    }

    public ResponseEntity<?> deleteById(Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);

        if (optionalEmployee.isPresent()) {
            try {
                employeeRepository.deleteById(id);
                return ResponseEntity.ok().body("Employee with ID " + id + " has been deleted successfully.");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the employee.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee with ID " + id + " not found.");
        }
    }

    public ResponseEntity<?> departmentOfEmployee(Long employeeId) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isPresent()) {
            Optional<Department> optionalDepartment = departmentRepository.findById(optionalEmployee.get().getDepartment().getId());
            try {
                Employee employee = optionalEmployee.get();
                Department department = optionalDepartment.get();
                String responseMessage = "Employee " + employee.getName() + " is associated with " + department.getDepartmentName();
                DepartmentResponse employeeResponse = new DepartmentResponse(department, HttpStatus.OK.value(), "", responseMessage);
                return ResponseEntity.status(HttpStatus.OK).body(employeeResponse);
            } catch (Exception e) {
                EmployeeResponse employeeResponse = new EmployeeResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(employeeResponse);
            }
        } else {
            EmployeeResponse employeeResponse = new EmployeeResponse(null, HttpStatus.NOT_FOUND.value(), "Not Found", "Employee or Department is not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(employeeResponse);
        }
    }


    public ResponseEntity<?> getHighestSalaryHolders() {
        List<Employee> employees = employeeRepository.findAll();

        Optional<Employee> highestSalaryEmployee = employees.stream()
                .max(Comparator.comparingDouble(Employee::getSalary));

        if (highestSalaryEmployee.isPresent()) {
            Employee employeeWithHighestSalary = highestSalaryEmployee.get();
            return ResponseEntity.status(HttpStatus.OK).body(employeeWithHighestSalary);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
        }
    }

    public ResponseEntity<?> getSecondHighestSalary() {
        List<Employee> employees = employeeRepository.findAll();

        Optional<Employee> secondHighestSalaryEmployee = employees.stream()
                .sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
                .skip(1)
                .findFirst();

        if (secondHighestSalaryEmployee.isPresent()) {
            Employee employeeWithSecondHighestSalary = secondHighestSalaryEmployee.get();
            return ResponseEntity.status(HttpStatus.OK).body(employeeWithSecondHighestSalary);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");

        }
    }

}
