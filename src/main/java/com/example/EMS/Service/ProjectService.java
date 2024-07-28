package com.example.EMS.Service;

import com.example.EMS.Entity.Employee;
import com.example.EMS.Entity.Project;
import com.example.EMS.Entity.ProjectStatus;
import com.example.EMS.Repository.EmployeeRepository;
import com.example.EMS.Repository.ProjectRepository;
import com.example.EMS.Response.EmployeeResponse;
import com.example.EMS.Response.ProjectResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProjectService {
    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    ProjectBudget projectBudget;

    @Autowired
    EmployeeDetails employeeDetails;

    public ResponseEntity<?> get() {
        List<Employee> employees = employeeRepository.findAll();
        List<Project> projectList = projectRepository.findAll();
        List<String> employeeNames = employees.stream().map(Employee::getName).toList();

        employeeDetails.setProject(projectList);
        employeeDetails.setEmployees(employeeNames);

        return ResponseEntity.status(HttpStatus.OK.value()).body(employeeDetails);
    }

    public ResponseEntity<?> add(Project project) {
        Project addedProject = projectRepository.save(project);
        ProjectResponse projectResponse = new ProjectResponse(addedProject, HttpStatus.OK.value(), "", "Project is Added");
        return ResponseEntity.status(HttpStatus.OK).body(projectResponse);
    }

    public ResponseEntity<?> updateById(Long projectId, Project projectDetails) {
        Optional<Project> optionalProject = projectRepository.findById((projectId));
        if (optionalProject.isPresent()) {
            System.out.println(projectDetails.getStatus() + "--" + optionalProject.get().getStatus());
            Project project = getProject(projectDetails, optionalProject);
            Project updatedProject = projectRepository.save(project);
            ProjectResponse projectResponse = new ProjectResponse(updatedProject, HttpStatus.OK.value(), "", "Project with Id " + projectId + " is updated");
            return ResponseEntity.status(HttpStatus.OK).body(projectResponse);
        } else {
            ProjectResponse projectResponse = new ProjectResponse(HttpStatus.NOT_FOUND.value(), "Not Found", "Project with Id " + projectId + " is not exists");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(projectResponse);
        }
    }

    private static Project getProject(Project projectDetails, Optional<Project> optionalProject) {
        Project project = optionalProject.get();
//        project.setProjectId(project.getProjectId() == 0 ? project.getProjectId() : projectDetails.getProjectId());
        project.setProjectName(projectDetails.getProjectName() == null ? project.getProjectName() : projectDetails.getProjectName());
        project.setStatus(projectDetails.getStatus() == null ? project.getStatus() : projectDetails.getStatus());
        project.setStartDate(projectDetails.getStartDate() == null ? project.getStartDate() : projectDetails.getStartDate());
        project.setEndDate(projectDetails.getEndDate() == null ? project.getEndDate() : projectDetails.getEndDate());
        return project;
    }

    public ResponseEntity<?> getById(Long projectId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            ProjectResponse projectResponse = new ProjectResponse(project, HttpStatus.OK.value(), "", "Project with Id " + projectId + " is Found");
            return ResponseEntity.status(HttpStatus.OK).body(projectResponse);
        } else {
            ProjectResponse projectResponse = new ProjectResponse(HttpStatus.NOT_FOUND.value(), "Not Found", "Project with Id " + projectId + " is not exists");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(projectResponse);
        }
    }

    public ResponseEntity<?> deleteByStatus() {

        List<Project> projectList = projectRepository.findAll();
        List<Long> projectIds = projectList.stream().filter(element -> element.getStatus() == ProjectStatus.ENDED).map(Project::getProjectId).collect(Collectors.toList());
        projectIds.forEach(element -> projectRepository.deleteById(element));
        return ResponseEntity.status(HttpStatus.OK).body(projectIds + " These projects have been Deleted");

    }

    public ResponseEntity<?> deleteByDate(Long id) {

        Optional<Project> optionalProject = projectRepository.findById(id);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            Date currentDate = new Date();
            if (currentDate.after(project.getEndDate())) {
                projectRepository.deleteById(id);
                return ResponseEntity.ok("Project with ID " + id + " has been deleted.");
            } else {
                return ResponseEntity.badRequest().body("Project with ID " + id + " cannot be deleted yet.");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<?> addEmployeeToProject(Long employeeId, Long projectId) {
        try {
            Optional<Project> optionalProject = projectRepository.findById(projectId);
            Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);

            if (optionalProject.isPresent() && optionalEmployee.isPresent()) {
                Project project = optionalProject.get();
                Employee employee = optionalEmployee.get();
//               For Team
                employee.getProjects().add(project);
                Employee savedEmployee = employeeRepository.save(employee);

                EmployeeResponse employeeResponse = new EmployeeResponse(savedEmployee, HttpStatus.OK.value(), null, "Employee is added");
                return ResponseEntity.ok().body(employeeResponse);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An internal server error occurred: " + e.getMessage());
        }
    }

    public ResponseEntity<?> getNewProjects() {
        List<Project> newProjects = projectRepository.findAll();
        Stream<Project> projectStream = newProjects.stream().filter(element -> element.getStatus() == ProjectStatus.NEW);
        return ResponseEntity.ok().body(projectStream);
    }

    public ResponseEntity<?> getOnGoingProjects() {
        List<Project> newProjects = projectRepository.findAll();
        Stream<Project> projectStream = newProjects.stream().filter(element -> element.getStatus() == ProjectStatus.ON_GOING);
        return ResponseEntity.ok().body(projectStream);
    }

    public ResponseEntity<?> getEndedProjects() {
        List<Project> newProjects = projectRepository.findAll();
        Stream<Project> projectStream = newProjects.stream().filter(element -> element.getStatus() == ProjectStatus.ENDED);
        return ResponseEntity.ok().body(projectStream);
    }

    public ResponseEntity<?> getProjectBudgetSalary() {
        List<Employee> employees = employeeRepository.findAll();
        Map<Project, Double> projectBudgetSalary = employees.stream()
                .flatMap(employee -> employee.getProjects().stream().map(project -> Map.entry(project, employee.getSalary())))
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingDouble(Map.Entry::getValue)));

        projectBudgetSalary.forEach((project, salary) -> {
            projectBudget.getProjectList().add(project);
            projectBudget.getSalary().add(salary);
        });

        return ResponseEntity.status(HttpStatus.OK.value()).body(projectBudget);
    }
}

@Component
class ProjectBudget {
    List<Project> projectList = new ArrayList<>();

    List<Double> salary = new ArrayList<>();

    public List<Project> getProjectList() {
        return projectList;
    }

    public List<Double> getSalary() {
        return salary;
    }

}

@Component
class EmployeeDetails {

    List<String> employees;

    List<Project> project;

    public void setEmployees(List<String> employees) {
        this.employees = employees;
    }

    public void setProject(List<Project> project) {
        this.project = project;
    }

    public List<String> getEmployees() {
        return employees;
    }

    public List<Project> getProject() {
        return project;
    }
}