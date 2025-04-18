package com.example.miniprojet_genie_logiciel.controllers;

import com.example.miniprojet_genie_logiciel.dto.EpicDTO;
import com.example.miniprojet_genie_logiciel.dto.UserStoryDTO;
import com.example.miniprojet_genie_logiciel.entities.Epic;
import com.example.miniprojet_genie_logiciel.mapper.EpicMapper;
import com.example.miniprojet_genie_logiciel.services.EpicService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/epics")
@RequiredArgsConstructor
public class EpicController {

    private final EpicService epicService;
    private final EpicMapper epicMapper;

    // Récupérer tous les epics
    @GetMapping
    public ResponseEntity<List<EpicDTO>> getAllEpics() {
        List<EpicDTO> epicDTOs = epicService.getAllEpics();
        return ResponseEntity.ok(epicDTOs);
    }

    // Récupérer un epic par son id
    @GetMapping("/{id}")
    public ResponseEntity<EpicDTO> getEpicById(@PathVariable Long id) {
        return epicService.getEpicById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Créer un nouvel epic
    @PostMapping
    public ResponseEntity<EpicDTO> createEpic(@RequestBody EpicDTO epicDTO) {
        EpicDTO created = epicService.createEpic(epicDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Mettre à jour un epic existant
    @PutMapping("/{id}")
    public ResponseEntity<EpicDTO> updateEpic(@PathVariable Long id, @RequestBody EpicDTO epicDTO) {
        epicDTO.setId(id);
        EpicDTO updated = epicService.updateEpic(epicDTO);
        return ResponseEntity.ok(updated);
    }

    // Supprimer un epic
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEpic(@PathVariable Long id) {
        try {
            epicService.deleteEpic(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Lier une User Story à un epic
    @PostMapping("/{epicId}/userstories")
    public ResponseEntity<EpicDTO> addUserStoryToEpic(@PathVariable Long epicId,
                                                   @RequestBody UserStoryDTO userStory) {
        try {
            EpicDTO updated = epicService.addUserStoryToEpic(epicId, userStory);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Visualiser les User Stories liées à un epic
    @GetMapping("/{epicId}/userstories")
    public ResponseEntity<List<UserStoryDTO>> getUserStoriesByEpic(@PathVariable Long epicId) {
        try {
            List<UserStoryDTO> userStories = epicService.getUserStoriesByEpic(epicId);
            return ResponseEntity.ok(userStories);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

