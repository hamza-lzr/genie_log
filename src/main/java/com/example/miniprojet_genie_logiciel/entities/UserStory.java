package com.example.miniprojet_genie_logiciel.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table
@Data
public class UserStory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String title;

    @Column
    private String role;

    @Column
    private String action;

    @Column
    private String value;

    @Column
    private String priority;

    @Column
    private String status;

    @ManyToOne
    private Epic epic;


}
