package org.example.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubtasksByTask {
    @PrimaryKey
    private String taskName;
    private UUID subTaskId;
}
