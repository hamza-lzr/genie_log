package com.example.miniprojet_genie_logiciel.repository;

import com.example.miniprojet_genie_logiciel.entities.ProductBacklog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductBacklogRepository extends JpaRepository<ProductBacklog, Long> {
}
