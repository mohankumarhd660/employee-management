package com.example.employeemanagement.controller;

import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private Employee employee1;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
        employee1 = new Employee(1L, "John Doe", "john.doe@example.com", "Engineering");
    }

    @Test
    @DisplayName("GET /api/employees - Should return 200 and list of employees")
    void getAllEmployees_shouldReturnStatus200AndList() throws Exception {
        Employee employee2 = new Employee(2L, "Jane Smith", "jane.smith@example.com", "Marketing");
        when(employeeService.getAllEmployees()).thenReturn(Arrays.asList(employee1, employee2));

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("John Doe")))
                .andExpect(jsonPath("$[1].name", is("Jane Smith")));
    }

    @Test
    @DisplayName("GET /api/employees/{id} - Should return 200 when employee exists")
    void getEmployeeById_whenExists_shouldReturnStatus200() throws Exception {
        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.of(employee1));

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("John Doe")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")));
    }

    @Test
    @DisplayName("GET /api/employees/{id} - Should return 404 when employee does not exist")
    void getEmployeeById_whenNotExists_shouldReturnStatus404() throws Exception {
        when(employeeService.getEmployeeById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/employees/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/employees - Should return 201 Created when employee is created")
    void createEmployee_shouldReturnStatus201() throws Exception {
        when(employeeService.createEmployee(any(Employee.class))).thenReturn(employee1);

        String jsonPayload = """
                {
                    "name": "John Doe",
                    "email": "john.doe@example.com",
                    "department": "Engineering"
                }
                """;

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("John Doe")));
    }

    @Test
    @DisplayName("PUT /api/employees/{id} - Should return 200 OK when updated successfully")
    void updateEmployee_whenExists_shouldReturnStatus200() throws Exception {
        Employee updatedEmployee = new Employee(1L, "John Updated", "john.updated@example.com", "DevOps");
        when(employeeService.updateEmployee(eq(1L), any(Employee.class))).thenReturn(Optional.of(updatedEmployee));

        String jsonPayload = """
                {
                    "name": "John Updated",
                    "email": "john.updated@example.com",
                    "department": "DevOps"
                }
                """;

        mockMvc.perform(put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John Updated")))
                .andExpect(jsonPath("$.department", is("DevOps")));
    }

    @Test
    @DisplayName("DELETE /api/employees/{id} - Should return 204 No Content when deleted")
    void deleteEmployee_whenExists_shouldReturnStatus204() throws Exception {
        when(employeeService.deleteEmployee(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/employees/{id} - Should return 404 Not Found when not found")
    void deleteEmployee_whenNotExists_shouldReturnStatus404() throws Exception {
        when(employeeService.deleteEmployee(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/employees/99"))
                .andExpect(status().isNotFound());
    }
}
