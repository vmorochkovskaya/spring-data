package org.example.app.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document("tasks")
@Data
@NoArgsConstructor
public class Task {
    @Id
    private String id;
    private LocalDate dateOfCreation;
    private LocalDate deadline;
    private String name;
    @TextIndexed(weight=1)
    private String description;
    private List<SubTask> subTasks;
    private String category;

    public Task(LocalDate dateOfCreation, LocalDate deadline, String name, String description, List<SubTask> subTasks, String category) {
        this.dateOfCreation = dateOfCreation;
        this.deadline = deadline;
        this.name = name;
        this.description = description;
        this.subTasks = subTasks;
        this.category = category;
    }
}
