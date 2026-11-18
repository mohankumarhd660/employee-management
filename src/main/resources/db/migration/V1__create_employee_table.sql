CREATE TABLE IF NOT EXISTS employees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    department VARCHAR(100) NOT NULL
);

-- Seed initial employee records
INSERT INTO employees (name, email, department) VALUES 
('John Doe', 'john.doe@example.com', 'Engineering'),
('Jane Smith', 'jane.smith@example.com', 'Marketing');
