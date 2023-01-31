package org.example.web;

import org.example.app.Employee;
import org.example.app.Salary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeesController {
    private static final List<Employee> HIRED_EMPLOYEES = List.of(Employee.builder()
                    .id("id-1")
                    .build(),
            Employee.builder()
                    .id("id-2")
                    .build(),
            Employee.builder()
                    .id("id-3")
                    .build(),
            Employee.builder()
                    .id("id-4")
                    .build());

    private static final List<Salary> SALARY = List.of(Salary.builder()
                    .employeeId("id-1")
                    .salary(new BigDecimal("1200.00"))
                    .build(),
            Salary.builder()
                    .employeeId("id-2")
                    .salary(new BigDecimal("1400.00"))
                    .build(),
            Salary.builder()
                    .employeeId("id-3")
                    .salary(new BigDecimal("1405.00"))
                    .build(),
            Salary.builder()
                    .employeeId("id-4")
                    .salary(new BigDecimal("1100.00"))
                    .build());

    @GetMapping("/")
    public List<Employee> getHiredEmployees() {
        return HIRED_EMPLOYEES;
    }

    @GetMapping("/salary/{id}")
    public Salary getSalary(@PathVariable("id") String employeeId) {
        return SALARY.stream()
                .filter(sal -> employeeId.equals(sal.getEmployeeId()))
                .findAny()
                .orElseThrow(UnsupportedOperationException::new);
    }
}
