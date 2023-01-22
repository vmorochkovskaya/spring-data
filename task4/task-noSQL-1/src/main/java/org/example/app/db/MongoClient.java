package org.example.app.db;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoClient {
    private static com.mongodb.client.MongoClient mongoClient;

    private MongoClient() {
    }

    public static synchronized com.mongodb.client.MongoClient getMongoClient() {
        if (mongoClient == null) {
            ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");
            CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
            CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
            MongoClientSettings clientSettings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .codecRegistry(codecRegistry)
                    .build();
            mongoClient = MongoClients.create(clientSettings);
        }
        return mongoClient;
    }

    public static void close() {
        mongoClient.close();
    }

}
