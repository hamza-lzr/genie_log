package com.example.miniprojet_genie_logiciel.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER)
    private ProductBacklog productBacklog;

}
