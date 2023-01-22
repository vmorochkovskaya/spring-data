package org.example.app.entity;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "user_entity")
public class User implements Serializable {
    private static final long serialVersionUID = 7526472295622776167L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String email;
}
