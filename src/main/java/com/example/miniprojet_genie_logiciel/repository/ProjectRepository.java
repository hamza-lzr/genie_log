package com.example.miniprojet_genie_logiciel.repository;


import com.example.miniprojet_genie_logiciel.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

}
