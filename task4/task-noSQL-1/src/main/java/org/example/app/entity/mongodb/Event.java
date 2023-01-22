package org.example.app.entity.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Event {
    @BsonProperty("id")
    private ObjectId id;
    private String title;
    private String date;
}
