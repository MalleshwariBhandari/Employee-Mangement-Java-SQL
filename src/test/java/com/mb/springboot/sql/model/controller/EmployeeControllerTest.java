package com.mb.springboot.sql.model.controller;

import com.mb.springboot.sql.model.constants.AppConstants;
import com.mb.springboot.sql.model.domain.Employee;
import com.mb.springboot.sql.model.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.mb.springboot.sql.model.constants.ErrorConstants.ERROR_DELETING_EMPLOYEE;
import static com.mb.springboot.sql.model.constants.ErrorConstants.ERROR_FETCHING_EMPLOYEE;
import static com.mb.springboot.sql.model.constants.ErrorConstants.ERROR_SAVING_EMPLOYEE_INFO;
import static com.mb.springboot.sql.model.constants.ErrorConstants.ERROR_FETCHING_EMPLOYEE_LIST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
    }

    @Test
    void testSaveEmployee_Success() {
        when(employeeService.saveEmployee(any(Employee.class))).thenReturn(AppConstants.EMPLOYEE_SAVED_SUCCESSFULLY);

        ResponseEntity<String> response = employeeController.saveEmployee(employee);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(AppConstants.EMPLOYEE_SAVED_SUCCESSFULLY, response.getBody());
    }

    @Test
    void testSaveEmployee_AlreadyExists() {
        when(employeeService.saveEmployee(any(Employee.class))).thenReturn(AppConstants.EMPLOYEE_ALREADY_EXISTS);

        ResponseEntity<String> response = employeeController.saveEmployee(employee);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(AppConstants.EMPLOYEE_ALREADY_EXISTS, response.getBody());
    }

    @Test
    void testSaveEmployee_Exception() {
        when(employeeService.saveEmployee(any(Employee.class))).thenThrow(new RuntimeException(ERROR_SAVING_EMPLOYEE_INFO));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            employeeController.saveEmployee(employee);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals(ERROR_SAVING_EMPLOYEE_INFO, exception.getReason());
    }

    @Test
    void testDeleteEmployee_Success() {
        when(employeeService.deleteEmployee(1L)).thenReturn(AppConstants.EMPLOYEE_DELETED_SUCCESSFULLY);

        ResponseEntity<String> response = employeeController.deleteEmployee(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(AppConstants.EMPLOYEE_DELETED_SUCCESSFULLY, response.getBody());
    }

    @Test
    void testDeleteEmployee_NotFound() {
        when(employeeService.deleteEmployee(1L)).thenReturn(AppConstants.EMPLOYEE_NOT_FOUND);

        ResponseEntity<String> response = employeeController.deleteEmployee(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(AppConstants.EMPLOYEE_NOT_FOUND, response.getBody());
    }

    @Test
    void testDeleteEmployee_Exception() {
        when(employeeService.deleteEmployee(1L)).thenThrow(new RuntimeException(ERROR_DELETING_EMPLOYEE));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            employeeController.deleteEmployee(1L);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals(ERROR_DELETING_EMPLOYEE, exception.getReason());
    }

    @Test
    void testGetEmployeeById_Success() {
        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.of(employee));

        ResponseEntity<?> response = employeeController.getEmployeeById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employee, response.getBody());
    }

    @Test
    void testGetEmployeeById_NotFound() {
        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = employeeController.getEmployeeById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(AppConstants.EMPLOYEE_NOT_FOUND, response.getBody());
    }

    @Test
    void testGetEmployeeById_Exception() {
        when(employeeService.getEmployeeById(1L)).thenThrow(new RuntimeException(ERROR_FETCHING_EMPLOYEE));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            employeeController.getEmployeeById(1L);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals(ERROR_FETCHING_EMPLOYEE, exception.getReason());
    }

    @Test
    void testGetEmployees_Success() {
        List<Employee> employees = Collections.singletonList(employee);
        when(employeeService.getAllEmployees()).thenReturn(employees);

        ResponseEntity<List<Employee>> response = employeeController.getEmployees();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employees, response.getBody());
    }

    @Test
    void testGetEmployees_Exception() {
        when(employeeService.getAllEmployees()).thenThrow(new RuntimeException(ERROR_FETCHING_EMPLOYEE_LIST));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            employeeController.getEmployees();
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals(ERROR_FETCHING_EMPLOYEE_LIST, exception.getReason());
    }
}
