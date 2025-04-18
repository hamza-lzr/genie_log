package com.example.miniprojet_genie_logiciel.mapper;

import com.example.miniprojet_genie_logiciel.dto.*;
import com.example.miniprojet_genie_logiciel.entities.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserStoryMapper {

    public UserStoryDTO toDto(UserStory userStory) {
        if (userStory == null) {
            return null;
        }

        UserStoryDTO dto = new UserStoryDTO();
        dto.setId(userStory.getId());
        dto.setTitle(userStory.getTitle());
        dto.setRole(userStory.getRole());
        dto.setAction(userStory.getAction());
        dto.setGoal(userStory.getGoal());
        dto.setAcceptanceCriteria(userStory.getAcceptanceCriteria());
        dto.setStoryPoints(userStory.getStoryPoints());

        if (userStory.getPriority() != null) {
            PriorityDTO priorityDTO = new PriorityDTO();
            priorityDTO.setName(userStory.getPriority().getLabel());
            priorityDTO.setWeight(userStory.getPriority().getWeight());
            dto.setPriority(priorityDTO);
        }

        if (userStory.getStatus() != null) {
            dto.setStatus(StatusDTO.valueOf(userStory.getStatus().toString()));
        }

        if (userStory.getEpic() != null) {
            EpicDTO epicDTO = new EpicDTO();
            epicDTO.setId(userStory.getEpic().getId());
            epicDTO.setTitle(userStory.getEpic().getTitle());
            dto.setEpic(epicDTO);
        }

        if (userStory.getTasks() != null) {
            List<TaskDTO> taskDTOs = userStory.getTasks().stream()
                .map(this::mapTaskToDTO)
                .collect(Collectors.toList());
            dto.setTasks(taskDTOs);
        }

        return dto;
    }

    public UserStory toEntity(UserStoryDTO dto) {
        if (dto == null) {
            return null;
        }

        UserStory userStory = new UserStory();
        userStory.setId(dto.getId());
        userStory.setTitle(dto.getTitle());
        userStory.setRole(dto.getRole());
        userStory.setAction(dto.getAction());
        userStory.setGoal(dto.getGoal());
        userStory.setAcceptanceCriteria(dto.getAcceptanceCriteria());
        userStory.setStoryPoints(dto.getStoryPoints());

        if (dto.getPriority() != null) {
            userStory.setPriority(Priority.fromLabel(dto.getPriority().getName()));
        }

        if (dto.getStatus() != null) {
            userStory.setStatus(Status.valueOf(dto.getStatus().toString()));
        }

        if (dto.getEpic() != null) {
            Epic epic = new Epic();
            epic.setId(dto.getEpic().getId());
            epic.setTitle(dto.getEpic().getTitle());
            userStory.setEpic(epic);
        }

        if (dto.getTasks() != null) {
            List<Task> tasks = dto.getTasks().stream()
                .map(this::mapDTOToTask)
                .collect(Collectors.toList());
            userStory.setTasks(tasks);
        }

        return userStory;
    }

    private TaskDTO mapTaskToDTO(Task task) {
        if (task == null) {
            return null;
        }
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(StatusDTO.valueOf(task.getStatus().toString()));
        dto.setEstimation(task.getEstimation());
        return dto;
    }

    private Task mapDTOToTask(TaskDTO dto) {
        if (dto == null) {
            return null;
        }
        Task task = new Task();
        task.setId(dto.getId());
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus().toString());
        task.setEstimation(dto.getEstimation());
        return task;
    }
}