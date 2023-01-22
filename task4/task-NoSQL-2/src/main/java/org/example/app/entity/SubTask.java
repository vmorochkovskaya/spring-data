package org.example.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.TextIndexed;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubTask {
    @TextIndexed(weight=2)
    private String name;
    private String description;
}
