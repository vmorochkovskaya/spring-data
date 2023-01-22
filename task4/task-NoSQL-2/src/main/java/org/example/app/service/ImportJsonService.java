package org.example.app.service;

import com.mongodb.MongoBulkWriteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service
public class ImportJsonService {
    @Autowired
    private MongoTemplate mongo;

    public List<Document> generateMongoDocs(List<String> jsons) {
        List<Document> docs = new ArrayList<>();
        for (String json : jsons) {
            docs.add(Document.parse(json));
        }
        return docs;    }

    public int insertInto(String collection, List<Document> mongoDocs) {
        try {
            Collection<Document> inserts = mongo.insert(mongoDocs, collection);
            return inserts.size();
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof MongoBulkWriteException) {
                return ((MongoBulkWriteException) e.getCause())
                        .getWriteResult()
                        .getInsertedCount();
            }
            return 0;
        }
    }
}
