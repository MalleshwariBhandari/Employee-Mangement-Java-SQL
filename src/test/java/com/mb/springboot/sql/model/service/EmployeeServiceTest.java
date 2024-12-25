package com.mb.springboot.sql.model.service;

import com.mb.springboot.sql.model.constants.AppConstants;
import com.mb.springboot.sql.model.domain.Employee;
import com.mb.springboot.sql.model.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeServiceImpl;

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
        when(employeeRepository.findByFirstNameAndLastName(anyString(), anyString())).thenReturn(Optional.empty());
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        String response = employeeServiceImpl.saveEmployee(employee);

        assertEquals(AppConstants.EMPLOYEE_SAVED_SUCCESSFULLY, response);
    }

    @Test
    void testSaveEmployee_AlreadyExists() {
        when(employeeRepository.findByFirstNameAndLastName(anyString(), anyString())).thenReturn(Optional.of(employee));

        String response = employeeServiceImpl.saveEmployee(employee);

        assertEquals(AppConstants.EMPLOYEE_ALREADY_EXISTS, response);
    }

    @Test
    void testDeleteEmployee_Success() {
        when(employeeRepository.existsById(1L)).thenReturn(true);

        String response = employeeServiceImpl.deleteEmployee(1L);

        assertEquals(AppConstants.EMPLOYEE_DELETED_SUCCESSFULLY, response);
        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteEmployee_NotFound() {
        when(employeeRepository.existsById(1L)).thenReturn(false);

        String response = employeeServiceImpl.deleteEmployee(1L);

        assertEquals(AppConstants.EMPLOYEE_NOT_FOUND, response);
        verify(employeeRepository, never()).deleteById(1L);
    }

    @Test
    void testGetEmployeeById_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Optional<Employee> result = employeeServiceImpl.getEmployeeById(1L);

        assertEquals(employee, result.get());
    }

    @Test
    void testGetEmployeeById_NotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Employee> result = employeeServiceImpl.getEmployeeById(1L);

        assertEquals(Optional.empty(), result);
    }

    @Test
    void testGetAllEmployees() {
        List<Employee> employees = Collections.singletonList(employee);
        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> result = employeeServiceImpl.getAllEmployees();

        assertEquals(employees, result);
    }

    @Test
    void testGetAllEmployees_EmptyList() {
        when(employeeRepository.findAll()).thenReturn(Collections.emptyList());

        List<Employee> result = employeeServiceImpl.getAllEmployees();

        assertEquals(Collections.emptyList(), result);
    }
}
