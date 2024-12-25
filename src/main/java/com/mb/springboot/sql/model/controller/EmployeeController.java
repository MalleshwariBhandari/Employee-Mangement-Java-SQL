package com.mb.springboot.sql.model.controller;

import com.mb.springboot.sql.model.constants.AppConstants;
import com.mb.springboot.sql.model.domain.Employee;
import com.mb.springboot.sql.model.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static com.mb.springboot.sql.model.constants.ErrorConstants.*;

@Slf4j
@RestController
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/saveEmployee")
    public ResponseEntity<String> saveEmployee(@RequestBody Employee employee) {
        try {
            String response = employeeService.saveEmployee(employee);
            if(response.equals(AppConstants.EMPLOYEE_SAVED_SUCCESSFULLY)){
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }else{
                return ResponseEntity.ok(response);
            }
        }catch (Exception e){
            log.error(ERROR_SAVING_EMPLOYEE_INFO, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,ERROR_SAVING_EMPLOYEE_INFO);
        }
    }

    @DeleteMapping("/deleteEmployee/{id}") public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        try {
            String response = employeeService.deleteEmployee(id);
            if (response.equals(AppConstants.EMPLOYEE_NOT_FOUND)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error(ERROR_DELETING_EMPLOYEE, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_DELETING_EMPLOYEE);
        }
    }

    @GetMapping("/getEmployeeById/{id}") public ResponseEntity<?> getEmployeeById(@PathVariable Long id) {
        try{
            Optional<Employee> employee = employeeService.getEmployeeById(id);
            if (employee.isPresent()){
                return ResponseEntity.ok(employee.get());
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(AppConstants.EMPLOYEE_NOT_FOUND);
            }
        }catch (Exception e){
            log.error(ERROR_FETCHING_EMPLOYEE, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_FETCHING_EMPLOYEE);
        }
    }

    @GetMapping("/getEmployees")
    public ResponseEntity<List<Employee>> getEmployees() {
        try{
            List<Employee> employeeList = employeeService.getAllEmployees();
            return ResponseEntity.ok(employeeList);
        }catch (Exception e){
            log.error(ERROR_FETCHING_EMPLOYEE_LIST,e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,ERROR_FETCHING_EMPLOYEE_LIST);
        }
    }
}