package org.example.app.repository;

import org.example.app.entity.SubtasksByTask;
import org.springframework.data.cassandra.repository.MapIdCassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubTasksByTaskRepository extends MapIdCassandraRepository<SubtasksByTask> {
    List<SubtasksByTask> findByTaskName(String name);
}
