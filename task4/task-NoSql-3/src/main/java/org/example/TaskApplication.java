package org.example;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import org.example.app.entity.SubTask;
import org.example.app.entity.Task;
import org.example.app.repository.CustomTaskRepository;
import org.example.app.repository.SubTasksByTaskRepository;
import org.example.app.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.List;

import static com.datastax.oss.driver.api.core.uuid.Uuids.timeBased;

@SpringBootApplication
public class TaskApplication implements CommandLineRunner {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private CustomTaskRepository customTaskRepository;
    @Autowired
    private SubTasksByTaskRepository subTaskRepository;

    public static void main(String[] args) {
        SpringApplication.run(TaskApplication.class, args);
    }

    @Override
    public void run(String... args) {
        // 1. INSERT
        String taskOneName = "task-1" + Uuids.random();
        SubTask subTask = new SubTask(timeBased(), "sub task-1", "desc sub 1");
        Task task = new Task(timeBased(),
                LocalDate.of(2021, 2, 3),
                LocalDate.of(2021, 2, 6),
                taskOneName,
                "desc-1",
                "Java",
                List.of(subTask));
        customTaskRepository.insert(task);

        SubTask subTask2 = new SubTask(timeBased(), "sub task-2", "desc sub 2");
        Task task2 = new Task(timeBased(),
                LocalDate.of(2021, 1, 18),
                LocalDate.of(2021, 1, 25),
                "task-2" + Uuids.random(),
                "desc-2",
                "Spring",
                List.of(subTask2));
        customTaskRepository.insert(task2);

        // 2. UPDATE
        Task taskBeforeUpdate = taskRepository.findByNameAndCategory(taskOneName, "Java");
        taskBeforeUpdate.setDeadline(LocalDate.of(2023, 10, 10));
        taskRepository.save(taskBeforeUpdate);

        // 2. READ
        System.out.println("========READ============");
        taskRepository.findAll().forEach(System.out::println);
        subTaskRepository.findByTaskName(taskOneName).forEach(System.out::println);

        //3. DELETE
        System.out.println("========DELETE============");
        taskRepository.deleteByNameAndCategory(taskOneName, "Java");
        taskRepository.findAll().forEach(System.out::println);
    }
}
