package com.example.miniprojet_genie_logiciel.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table
public class Epic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private Status status;

    @JsonIgnore
    @OneToMany(mappedBy = "epic")
    private List<UserStory> userStories;
}
