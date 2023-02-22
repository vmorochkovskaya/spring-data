package org.example.app.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
public class LiquidOrder extends Order {
    private int volume;
    @Builder.Default
    private GoodType type = GoodType.LIQUID;
}
