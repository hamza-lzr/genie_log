package com.example.miniprojet_genie_logiciel.repository;

import com.example.miniprojet_genie_logiciel.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
