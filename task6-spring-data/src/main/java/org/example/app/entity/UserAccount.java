package org.example.app.entity;

import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "user_account")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserAccount {
    @Id
    private int id;
    private BigDecimal amount;

    @JoinColumn(name = "user_id",
            columnDefinition = "INT NOT NULL")
    @OneToOne
    private User user;
}
