package com.example.miniprojet_genie_logiciel.repositories;

import com.example.miniprojet_genie_logiciel.entities.Epic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EpicRepository extends JpaRepository<Epic, Long> {

    Optional<Epic> deleteEpicById(long id);
}
