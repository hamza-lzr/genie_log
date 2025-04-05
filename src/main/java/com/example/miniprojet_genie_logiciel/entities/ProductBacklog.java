package com.example.miniprojet_genie_logiciel.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductBacklog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;

    @OneToOne
    private Project project;

    @JsonIgnore
    @OneToMany
    private List<Epic> epics;

    @JsonIgnore
    @OneToMany(mappedBy = "productBacklog")
    private List<UserStory> userStories;
}
