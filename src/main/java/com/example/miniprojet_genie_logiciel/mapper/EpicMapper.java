package com.example.miniprojet_genie_logiciel.mapper;

import com.example.miniprojet_genie_logiciel.dto.CreateEpicDTO;
import com.example.miniprojet_genie_logiciel.dto.EpicDTO;
import com.example.miniprojet_genie_logiciel.dto.UpdateEpicDTO;
import com.example.miniprojet_genie_logiciel.entities.Epic;
import org.springframework.stereotype.Component;

@Component
public class EpicMapper {

    public EpicDTO toDto(Epic epic) {
        if (epic == null) return null;

        EpicDTO dto = new EpicDTO();
        dto.setId(epic.getId());
        dto.setTitle(epic.getTitle());
        dto.setDescription(epic.getDescription());
        dto.setStatus(epic.getStatus());
        return dto;
    }

    public Epic toEntity(EpicDTO dto) {
        if (dto == null) return null;

        Epic epic = new Epic();
        epic.setId(dto.getId());
        epic.setTitle(dto.getTitle());
        epic.setDescription(dto.getDescription());
        epic.setStatus(dto.getStatus());
        return epic;
    }

    public Epic fromCreateDto(CreateEpicDTO dto) {
        if (dto == null) return null;

        Epic epic = new Epic();
        epic.setTitle(dto.getTitle());
        epic.setDescription(dto.getDescription());
        epic.setStatus(dto.getStatus());
        return epic;
    }

    public void updateFromDto(UpdateEpicDTO dto, Epic epic) {
        if (dto == null || epic == null) return;

        epic.setTitle(dto.getTitle());
        epic.setDescription(dto.getDescription());
        epic.setStatus(dto.getStatus());
    }
}


