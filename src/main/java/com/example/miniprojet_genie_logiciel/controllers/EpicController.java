package com.example.miniprojet_genie_logiciel.controllers;

import com.example.miniprojet_genie_logiciel.dto.CreateEpicDTO;
import com.example.miniprojet_genie_logiciel.dto.EpicDTO;
import com.example.miniprojet_genie_logiciel.dto.UpdateEpicDTO;
import com.example.miniprojet_genie_logiciel.services.EpicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/epics")
@RequiredArgsConstructor
public class EpicController {

    private final EpicService epicService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_OWNER')")
    public ResponseEntity<EpicDTO> createEpic(@Valid @RequestBody CreateEpicDTO dto) {
        return ResponseEntity.ok(epicService.createEpic(dto));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER', 'SCRUM_MASTER', 'DEVELOPER')")
    public ResponseEntity<List<EpicDTO>> getAllEpics() {
        return ResponseEntity.ok(epicService.getAllEpics());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER', 'SCRUM_MASTER', 'DEVELOPER')")
    public ResponseEntity<EpicDTO> getEpicById(@PathVariable Long id) {
        return ResponseEntity.ok(epicService.getEpicById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_OWNER')")
    public ResponseEntity<EpicDTO> updateEpic(@PathVariable Long id, @Valid @RequestBody UpdateEpicDTO dto) {
        return ResponseEntity.ok(epicService.updateEpic(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_OWNER')")
    public ResponseEntity<Void> deleteEpic(@PathVariable Long id) {
        epicService.deleteEpic(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-backlog/{backlogId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER', 'SCRUM_MASTER')")
    public ResponseEntity<List<EpicDTO>> getEpicsByBacklog(@PathVariable Long backlogId) {
        return ResponseEntity.ok(epicService.getEpicsByBacklogId(backlogId));
    }
}


