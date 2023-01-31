package org.example.service;

import org.example.app.Employee;
import org.example.app.Salary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class EmployeeService {
    @Autowired
    private RestTemplate restTemplate;

    public void consumeEndpoint() {
        CompletableFuture<List<CompletableFuture<Employee>>> hiredEmployeesFuture = CompletableFuture.supplyAsync(() -> {
                    return restTemplate.getForObject(
                            "http://localhost:8080/employees/", Employee[].class);
                })
                .thenApplyAsync(employees -> {
                    List<CompletableFuture<Employee>> cf = Stream.of(employees)
                            .map(employee -> {
                                return CompletableFuture.supplyAsync(() -> {
                                    Salary salary = restTemplate.getForObject("http://localhost:8080/employees/salary/" + employee.getId(),
                                            Salary.class);
                                    employee.setSalary(salary);
                                    return employee;
                                });
                            })
                            .collect(Collectors.toList());
                    return cf;
                });

        hiredEmployeesFuture.join().stream()
                .map(CompletableFuture::join)
                .forEach(System.out::println);

    }

}
