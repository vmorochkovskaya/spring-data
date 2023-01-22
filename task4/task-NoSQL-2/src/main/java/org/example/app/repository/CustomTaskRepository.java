package org.example.app.repository;

import org.example.app.entity.SubTask;
import org.example.app.entity.Task;

import java.time.LocalDate;
import java.util.List;

public interface CustomTaskRepository {
    void updateTaskDeadline(String name, LocalDate deadline);
    void saveSubtask(String name, SubTask subTask);
    void updateSubTaskName(String subTaskName, String newSubTaskName);
    void deleteSubTask(String subTaskName);

    List<Task> findTasksByWord(String word);

}
