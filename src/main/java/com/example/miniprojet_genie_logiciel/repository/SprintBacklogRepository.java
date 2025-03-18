package com.example.miniprojet_genie_logiciel.repository;

import com.example.miniprojet_genie_logiciel.entities.SprintBacklog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SprintBacklogRepository extends JpaRepository<SprintBacklog, Long> {
}

