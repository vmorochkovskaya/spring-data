package org.example;

import org.bson.Document;
import org.example.app.entity.SubTask;
import org.example.app.entity.Task;
import org.example.app.repository.CustomTaskRepository;
import org.example.app.repository.TaskRepository;
import org.example.app.service.ImportJsonService;
import org.example.app.utils.ImportUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


@SpringBootApplication
@EnableMongoRepositories
public class TaskApplication implements CommandLineRunner{
    @Autowired
    private ImportJsonService importJsonService;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private CustomTaskRepository customTaskRepository;

    public static void main(String[] args) {
        SpringApplication.run(TaskApplication.class, args);
    }

    @Override
    public void run(String...args) throws IOException {
        //0. import from Json to MongoDB
        String task1Json = ImportUtils.getStringFromResource("json/task-1.json");
        String task2Json = ImportUtils.getStringFromResource("json/task-2.json");
        String task3Json = ImportUtils.getStringFromResource("json/task-3.json");
        List<Document> generateMongoDocs = importJsonService.generateMongoDocs(List.of(task1Json, task2Json, task3Json));
        importJsonService.insertInto("tasks", generateMongoDocs);

        //1. Display on console all tasks.
        System.out.println("---------ALL TASKS---------");
        taskRepository.findAll().forEach(System.out::println);

        //2. Display overdue tasks.
        System.out.println("---------OVERDUE TASKS---------");
        taskRepository.findAllByDeadlineBefore(LocalDate.now()).forEach(System.out::println);

        //3. Display all tasks with the specific category (query parameter).
        System.out.println("---------SPECIFIC CATEGORY TASKS---------");
        taskRepository.getByCategory("AWS").forEach(System.out::println);

        //4. Display all subtasks related to tasks with the specific category (query parameter).
        System.out.println("---------SUBTASKS BY TASK CATEGORY---------");
        taskRepository.getSubTasksByTaskCategory("Java").forEach(System.out::println);

        //5. Perform insert/update/delete of the task.
        System.out.println("---------INSERT TASK---------");
        System.out.println(taskRepository.save(new Task(LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 8),
                "task-new",
                "desc",
                List.of(new SubTask("name", "desc")),
                "Spring")));
        System.out.println("---------UPDATE TASK---------");
        customTaskRepository.updateTaskDeadline("task-2", LocalDate.of(2023, 1, 11));
        System.out.println("---------DELETE TASK---------");
        taskRepository.deleteByName("task-new");

        //6. Perform insert/update/delete all subtasks of the given task (query parameter).
        System.out.println("---------INSERT SUB TASK---------");
        customTaskRepository.saveSubtask("task-2", new SubTask("sub task-23", "desc"));
        System.out.println("---------UPDATE SUB TASK---------");
        customTaskRepository.updateSubTaskName("sub task-22", "sub task-22-new");
        System.out.println("---------DELETE SUB TASK---------");
        customTaskRepository.deleteSubTask("sub task-31");

        //7. Support full-text search by word in task description.
        System.out.println("---------full-text search by word in task description---------");
        customTaskRepository.findTasksByWord("description").forEach(System.out::println);

    }
}
