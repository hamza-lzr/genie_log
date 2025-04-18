package com.example.miniprojet_genie_logiciel.mapper;

import com.example.miniprojet_genie_logiciel.dto.TaskDTO;
import com.example.miniprojet_genie_logiciel.dto.UserStoryDTO;
import com.example.miniprojet_genie_logiciel.dto.StatusDTO;
import com.example.miniprojet_genie_logiciel.entities.Task;
import com.example.miniprojet_genie_logiciel.entities.UserStory;
import com.example.miniprojet_genie_logiciel.entities.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    @Autowired
    private StatusMapper statusMapper;

    public TaskDTO toDto(Task task) {
        if (task == null) {
            return null;
        }

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(task.getId());
        taskDTO.setTitle(task.getTitle());
        taskDTO.setDescription(task.getDescription());
        if (task.getStatus() != null) {
            taskDTO.setStatus(statusMapper.toDto(Status.valueOf(task.getStatus())));
        }
        taskDTO.setEstimation(task.getEstimation());

        if (task.getUserStory() != null) {
            UserStoryDTO userStoryDTO = new UserStoryDTO();
            userStoryDTO.setId(task.getUserStory().getId());
            userStoryDTO.setTitle(task.getUserStory().getTitle());
            taskDTO.setUserStory(userStoryDTO);
        }

        return taskDTO;
    }

    public Task toEntity(TaskDTO taskDTO) {
        if (taskDTO == null) {
            return null;
        }

        Task task = new Task();
        task.setId(taskDTO.getId());
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        Status status = statusMapper.toEntity(taskDTO.getStatus());
        task.setStatus(status != null ? status.name() : null);
        task.setEstimation(taskDTO.getEstimation());

        if (taskDTO.getUserStory() != null) {
            UserStory userStory = new UserStory();
            userStory.setId(taskDTO.getUserStory().getId());
            userStory.setTitle(taskDTO.getUserStory().getTitle());
            task.setUserStory(userStory);
        }

        return task;
    }
}