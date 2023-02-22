package org.example.app.entity;


import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MessageResponse {
    @SerializedName("Message")
    private String order;
    @SerializedName("Timestamp")
    private LocalDateTime timestamp;
}
