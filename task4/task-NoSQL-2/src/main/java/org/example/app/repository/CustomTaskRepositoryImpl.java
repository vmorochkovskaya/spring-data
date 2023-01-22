package org.example.app.repository;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import org.example.app.entity.SubTask;
import org.example.app.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
public class CustomTaskRepositoryImpl implements CustomTaskRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void updateTaskDeadline(String name, LocalDate deadline) {
        Query query = new Query(where("name").is(name));
        Update update = new Update();
        update.set("deadline", deadline);
        mongoTemplate.updateMulti(query, update, Task.class);
    }

    @Override
    public void saveSubtask(String name, SubTask subTask) {
        Query query = new Query(where("name").is(name));
        Update update = new Update();
        update.push("subTasks", subTask);
        mongoTemplate.updateMulti(query, update, Task.class);
    }

    @Override
    public void updateSubTaskName(String subTaskName, String newSubTaskName) {
        Query query = Query.query(Criteria
                .where("subTasks")
                .elemMatch(
                        Criteria.where("name").is(subTaskName)
                )
        );
        Update update = new Update();
        update.set("subTasks.$.name", newSubTaskName);
        mongoTemplate.updateMulti(query, update, Task.class);
    }

    @Override
    public void deleteSubTask(String subTaskName) {
        Query query = Query.query(Criteria.where("subTasks")
                .elemMatch(
                        Criteria.where("name").is(subTaskName)
                )
        );
        DBObject pullUpdate =
                BasicDBObjectBuilder.start().add(
                        "name",
                        BasicDBObjectBuilder.start()
                                .add("$eq",
                                        subTaskName).get()).get();

        Update update = new Update();
        update.pull("subTasks", pullUpdate);
        mongoTemplate.updateMulti(query, update, Task.class);
    }

    @Override
    public List<Task> findTasksByWord(String word) {
        TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(word);
        Query query = TextQuery.queryText(criteria);
        return mongoTemplate.find(query, Task.class);
    }
}
