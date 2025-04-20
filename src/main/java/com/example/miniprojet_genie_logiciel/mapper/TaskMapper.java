package com.example.miniprojet_genie_logiciel.mapper;

import com.example.miniprojet_genie_logiciel.dto.CreateTaskDTO;
import com.example.miniprojet_genie_logiciel.dto.TaskDTO;
import com.example.miniprojet_genie_logiciel.dto.UpdateTaskDTO;
import com.example.miniprojet_genie_logiciel.entities.Task;
import com.example.miniprojet_genie_logiciel.entities.UserStory;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskDTO toDto(Task task) {
        if (task == null) return null;

        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setEstimation(task.getEstimation());
        dto.setUserStoryId(task.getUserStory() != null ? task.getUserStory().getId() : null);
        return dto;
    }

    public Task fromCreateDto(CreateTaskDTO dto, UserStory userStory) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setEstimation(dto.getEstimation());
        task.setUserStory(userStory);
        return task;
    }

    public void updateFromDto(UpdateTaskDTO dto, Task task) {
        if (dto.getTitle() != null) task.setTitle(dto.getTitle());
        if (dto.getDescription() != null) task.setDescription(dto.getDescription());
        if (dto.getStatus() != null) task.setStatus(dto.getStatus());
        if (dto.getEstimation() != null) task.setEstimation(dto.getEstimation());
    }
}
