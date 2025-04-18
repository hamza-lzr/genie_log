package com.example.miniprojet_genie_logiciel.mapper;

import com.example.miniprojet_genie_logiciel.dto.PriorityDTO;
import com.example.miniprojet_genie_logiciel.entities.Priority;
import org.springframework.stereotype.Component;

@Component
public class PriorityMapper {

    public PriorityDTO toDto(Priority priority) {
        if (priority == null) {
            return null;
        }

        PriorityDTO dto = new PriorityDTO();
        dto.setName(priority.getLabel());
        dto.setWeight(priority.getWeight());
        return dto;
    }

    public Priority toEntity(PriorityDTO dto) {
        if (dto == null) {
            return null;
        }

        return Priority.fromLabel(dto.getName());
    }
}

