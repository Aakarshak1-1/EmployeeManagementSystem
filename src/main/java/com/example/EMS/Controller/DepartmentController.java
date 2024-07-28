package com.example.EMS.Controller;

import com.example.EMS.Entity.Department;
import com.example.EMS.Entity.Department;
import com.example.EMS.Service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(("/v1/department"))

public class DepartmentController {
    @Autowired
    DepartmentService departmentService;

    @GetMapping("/fetchDepartment")
    public ResponseEntity<?> fetchDepartment() {
        return departmentService.get();
    }

    @PostMapping("/createDepartment")
    public ResponseEntity<?> createDepartment(@RequestBody Department department) {
        System.out.println(department);
        return departmentService.add(department);
    }

    @PutMapping("/updateDepartment/{id}")
    public ResponseEntity<?> updateDepartmentById(@PathVariable(value = "id") Long departmentId, @RequestBody Department departmentDetails) {
        return departmentService.updateById(departmentId, departmentDetails);
    }

    @GetMapping("/departmentOfEmployees/{departmentId}")
    public ResponseEntity<?> departmentOfEmployees(@PathVariable(value = "departmentId") Long departmentId) {
        return departmentService.getDepartmentWithEmployees(departmentId);
    }

    @DeleteMapping("/delete/{departmentId}")
    public ResponseEntity<?> deleteDepartment(@PathVariable(value = "departmentId") Long departmentId) {
        return departmentService.deleteDepartment(departmentId);
    }

    @GetMapping("/total-salary")
    public ResponseEntity<?> getTotalSalaryByDepartment() {
        return departmentService.getTotalSalaryByDepartment();
    }
}
