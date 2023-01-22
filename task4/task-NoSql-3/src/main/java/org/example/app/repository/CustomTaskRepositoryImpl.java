package org.example.app.repository;

import org.example.app.entity.SubTask;
import org.example.app.entity.SubtasksByTask;
import org.example.app.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraBatchOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class CustomTaskRepositoryImpl implements CustomTaskRepository {
    @Autowired
    private CassandraTemplate cassandraTemplate;

    @Override
    public void insert(Task task) {
        CassandraBatchOperations batchOps = cassandraTemplate.batchOps();
        insertSubTasksByTask(task, batchOps);
        batchOps.insert(task);
        batchOps.execute();
    }

    private void insertSubTasksByTask(Task task, final CassandraBatchOperations batchOps) {
        task.getSubTasks()
                .forEach(
                        subTask -> {
                            batchOps.insert(
                                    new SubtasksByTask(task.getName(), subTask.getId()));
                            batchOps.insert(
                                    new SubTask(subTask.getId(), subTask.getName(), subTask.getDescription()));
                        });
    }
}
