package com.example.EMS.Response;

import com.example.EMS.Entity.Employee;
import org.springframework.http.ResponseEntity;

public class EmployeeResponse {

    private Employee data;
    private int status_code;
    private String error;
    private String message;


    public EmployeeResponse(int status_code, String error, String message) {
        this.status_code = status_code;
        this.error = error;
        this.message = message;
    }

    public EmployeeResponse(Employee data, int status_code, String error, String message) {
        this.data = data;
        this.status_code = status_code;
        this.error = error;
        this.message = message;
    }

    public Employee getData() {
        return data;
    }

    public void setData(Employee data) {
        this.data = data;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
