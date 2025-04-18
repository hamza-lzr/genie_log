package com.example.miniprojet_genie_logiciel.mapper;

import com.example.miniprojet_genie_logiciel.dto.StatusDTO;
import com.example.miniprojet_genie_logiciel.entities.Status;
import org.springframework.stereotype.Component;

@Component
public class StatusMapper {

    public StatusDTO toDto(Status status) {
        if (status == null) {
            return null;
        }

        switch (status) {
            case TO_DO:
                return StatusDTO.TO_DO;
            case IN_PROGRESS:
                return StatusDTO.IN_PROGRESS;
            case DONE:
                return StatusDTO.DONE;
            default:
                throw new IllegalArgumentException("Status non reconnu : " + status);
        }
    }

    public Status toEntity(StatusDTO dto) {
        if (dto == null) {
            return null;
        }

        switch (dto) {
            case TO_DO:
                return Status.TO_DO;
            case IN_PROGRESS:
                return Status.IN_PROGRESS;
            case DONE:
                return Status.DONE;
            case BLOCKED:
                return Status.TO_DO; // Par défaut, on mappe BLOCKED vers TO_DO
            default:
                throw new IllegalArgumentException("StatusDTO non reconnu : " + dto);
        }
    }
}


