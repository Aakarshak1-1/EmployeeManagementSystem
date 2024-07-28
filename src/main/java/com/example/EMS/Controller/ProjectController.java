package com.example.EMS.Controller;

import com.example.EMS.Entity.Project;
import com.example.EMS.Service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/v1/project")
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @GetMapping("/fetchProject")
    public ResponseEntity<?> fetchProject() {
        return projectService.get();
    }

    @PostMapping("/createProject")
    public ResponseEntity<?> createProject(@RequestBody Project project) {
        return projectService.add(project);
    }

    @GetMapping("/findProject/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable(value = "id") Long projectId) {
        return projectService.getById(projectId);
    }

    @PutMapping("/updateProject/{id}")
    public ResponseEntity<?> updateProjectById(@PathVariable(value = "id") Long projectId, @RequestBody Project projectDetails) {
        return projectService.updateById(projectId, projectDetails);
    }

    @DeleteMapping("/deleteByStatus/")
    public ResponseEntity<?> deleteByStatus() {
        return projectService.deleteByStatus();
    }

    @DeleteMapping("/deleteByDate/{id}")
    public ResponseEntity<?> deleteByDate(@PathVariable Long id) {
        return projectService.deleteByDate(id);
    }

    @PutMapping("/addTeam/employee/{employeeId}/project/{projectId}")
    public ResponseEntity<?> addTeam(@PathVariable Long employeeId, @PathVariable Long projectId) {
        return projectService.addEmployeeToProject(employeeId, projectId);
    }

    @GetMapping("/new-projects")
    public ResponseEntity<?> getNewProjects() {
        return projectService.getNewProjects();
    }

    @GetMapping("/ongoing-projects")
    public ResponseEntity<?> getOnGoingProjects() {
        return projectService.getOnGoingProjects();
    }

    @GetMapping("/ended-projects")
    public ResponseEntity<?> getEndedProjects() {
        return projectService.getEndedProjects();
    }

    @GetMapping("project-budget-salary")
    public ResponseEntity<?> getProjectBudgetSalary() {
        return projectService.getProjectBudgetSalary();
    }
}
