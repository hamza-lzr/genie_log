package com.example.miniprojet_genie_logiciel.mapper;

import com.example.miniprojet_genie_logiciel.dto.Status;
import org.springframework.stereotype.Component;

@Component
public class StatusMapper {

    public Status toDto(com.example.miniprojet_genie_logiciel.entities.Status status) {
        if (status == null) {
            return null;
        }

        switch (status) {
            case TO_DO:
                return Status.TO_DO;
            case IN_PROGRESS:
                return Status.IN_PROGRESS;
            case DONE:
                return Status.DONE;
            default:
                throw new IllegalArgumentException("Status non reconnu : " + status);
        }
    }

    public com.example.miniprojet_genie_logiciel.entities.Status toEntity(Status dto) {
        if (dto == null) {
            return null;
        }

        switch (dto) {
            case TO_DO:
                return com.example.miniprojet_genie_logiciel.entities.Status.TO_DO;
            case IN_PROGRESS:
                return com.example.miniprojet_genie_logiciel.entities.Status.IN_PROGRESS;
            case DONE:
                return com.example.miniprojet_genie_logiciel.entities.Status.DONE;
            case BLOCKED:
                return com.example.miniprojet_genie_logiciel.entities.Status.TO_DO; // Par défaut, on mappe BLOCKED vers TO_DO
            default:
                throw new IllegalArgumentException("StatusDTO non reconnu : " + dto);
        }
    }
}


