package com.example.miniprojet_genie_logiciel.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Table
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String status;
    private Date startDate;
    private Date endDate;

    @OneToOne
    private ProductBacklog productBacklog;

}
