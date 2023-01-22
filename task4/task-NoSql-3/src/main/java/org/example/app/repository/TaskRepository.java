package org.example.app.repository;


import org.example.app.entity.Task;
import org.springframework.data.cassandra.repository.MapIdCassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends MapIdCassandraRepository<Task> {
    Task findByNameAndCategory(String name, String cat);
    boolean deleteByNameAndCategory(String name, String cat);

}
