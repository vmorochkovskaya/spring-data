package org.example.app.repository;

import org.example.app.entity.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findAll();

    @Query("{'deadline' : { $lt: ?0} }")
    List<Task> findAllByDeadlineBefore(LocalDate from);

    @Query("{'category' : ?0 }")
    List<Task> getByCategory(String cat);

    @Query(value="{'category' : ?0 }", fields="{'category': 1, 'subTasks' : 1}")
    List<Task> getSubTasksByTaskCategory(String cat);

    Task save(Task task);
    void deleteByName(String name);
}
