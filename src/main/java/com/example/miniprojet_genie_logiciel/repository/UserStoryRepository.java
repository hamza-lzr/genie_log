package com.example.miniprojet_genie_logiciel.repository;

import com.example.miniprojet_genie_logiciel.entities.UserStory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStoryRepository extends JpaRepository<UserStory, Long> {
}
