package com.example.employeemanagement;

import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EmployeeManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeManagementApplication.class, args);
    }

    // Seeds initial data into MySQL if the table is empty
    @Bean
    CommandLineRunner initDatabase(EmployeeRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new Employee(null, "John Doe", "john.doe@example.com", "Engineering"));
                repository.save(new Employee(null, "Jane Smith", "jane.smith@example.com", "Marketing"));
                System.out.println(">>> Seeded 2 default employees into MySQL 'employees' table.");
            }
        };
    }
}
