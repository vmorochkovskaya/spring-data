package org.example.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
public class SubTask {
    @PrimaryKey
    private UUID id;
    private String name;
    private String description;
}
