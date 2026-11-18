package com.example.employeemanagement.service;

import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee1;
    private Employee employee2;

    @BeforeEach
    void setUp() {
        employee1 = new Employee(1L, "John Doe", "john.doe@example.com", "Engineering");
        employee2 = new Employee(2L, "Jane Smith", "jane.smith@example.com", "Marketing");
    }

    @Test
    @DisplayName("Should return all employees")
    void getAllEmployees_shouldReturnAllEmployees() {
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee1, employee2));

        List<Employee> result = employeeService.getAllEmployees();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("John Doe");
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return employee by ID when found")
    void getEmployeeById_whenFound_shouldReturnEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));

        Optional<Employee> result = employeeService.getEmployeeById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("john.doe@example.com");
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should return empty Optional when employee ID not found")
    void getEmployeeById_whenNotFound_shouldReturnEmpty() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Employee> result = employeeService.getEmployeeById(99L);

        assertThat(result).isEmpty();
        verify(employeeRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Should save and return a new employee")
    void createEmployee_shouldSaveAndReturnEmployee() {
        Employee newEmployee = new Employee(null, "Alice Brown", "alice@example.com", "HR");
        Employee savedEmployee = new Employee(3L, "Alice Brown", "alice@example.com", "HR");

        when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmployee);

        Employee result = employeeService.createEmployee(newEmployee);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getName()).isEqualTo("Alice Brown");
        verify(employeeRepository, times(1)).save(newEmployee);
    }

    @Test
    @DisplayName("Should update existing employee")
    void updateEmployee_whenExists_shouldUpdateAndReturn() {
        Employee updateDetails = new Employee(null, "John Updated", "john.updated@example.com", "DevOps");
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee1);

        Optional<Employee> result = employeeService.updateEmployee(1L, updateDetails);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("John Updated");
        assertThat(result.get().getDepartment()).isEqualTo("DevOps");
        verify(employeeRepository, times(1)).save(employee1);
    }

    @Test
    @DisplayName("Should return empty Optional when updating non-existent employee")
    void updateEmployee_whenNotExists_shouldReturnEmpty() {
        Employee updateDetails = new Employee(null, "Unknown", "unknown@example.com", "IT");
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Employee> result = employeeService.updateEmployee(99L, updateDetails);

        assertThat(result).isEmpty();
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    @DisplayName("Should delete employee and return true when exists")
    void deleteEmployee_whenExists_shouldReturnTrue() {
        when(employeeRepository.existsById(1L)).thenReturn(true);
        doNothing().when(employeeRepository).deleteById(1L);

        boolean result = employeeService.deleteEmployee(1L);

        assertThat(result).isTrue();
        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should return false when deleting non-existent employee")
    void deleteEmployee_whenNotExists_shouldReturnFalse() {
        when(employeeRepository.existsById(99L)).thenReturn(false);

        boolean result = employeeService.deleteEmployee(99L);

        assertThat(result).isFalse();
        verify(employeeRepository, never()).deleteById(anyLong());
    }
}
