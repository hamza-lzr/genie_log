package com.example.miniprojet_genie_logiciel.services;

import com.example.miniprojet_genie_logiciel.entities.Epic;
import com.example.miniprojet_genie_logiciel.entities.Priority;
import com.example.miniprojet_genie_logiciel.entities.Status;
import com.example.miniprojet_genie_logiciel.entities.UserStory;
import com.example.miniprojet_genie_logiciel.entities.ProductBacklog;
import com.example.miniprojet_genie_logiciel.repository.EpicRepository;
import com.example.miniprojet_genie_logiciel.repository.ProductBacklogRepository;
import com.example.miniprojet_genie_logiciel.repository.UserStoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserStoryService {

    private final UserStoryRepository userStoryRepository;
    private final EpicRepository epicRepository;
    private final ProductBacklogRepository productBacklogRepository;

    // ==== CRUD Operations ====
    public UserStory addUserStory(@Valid UserStory userStory) {
        validateUserStory(userStory);
        return userStoryRepository.save(userStory);
    }

    public UserStory updateUserStory(Long id, @Valid UserStory updatedStory) {
        UserStory existing = getUserStoryOrThrow(id);
        updateFields(existing, updatedStory);
        return userStoryRepository.save(existing);
    }

    public void deleteUserStory(Long id) {
        if (!userStoryRepository.existsById(id)) {
            throw new EntityNotFoundException("User Story non trouvée avec l'ID : " + id);
        }
        userStoryRepository.deleteById(id);
    }

    // ==== Business Logic ====
    public UserStory linkUserStoryToEpic(Long userStoryId, Long epicId) {
        UserStory userStory = getUserStoryOrThrow(userStoryId);
        Epic epic = epicRepository.findById(epicId)
                .orElseThrow(() -> new EntityNotFoundException("Epic non trouvé avec l'ID : " + epicId));
        userStory.setEpic(epic);
        return userStoryRepository.save(userStory);
    }

    public UserStory setAcceptanceCriteria(Long userStoryId, String criteria) {
        UserStory userStory = getUserStoryOrThrow(userStoryId);
        userStory.setAcceptanceCriteria(criteria);
        return userStoryRepository.save(userStory);
    }

    public UserStory updateUserStoryStatus(Long userStoryId, Status status) {
        UserStory userStory = getUserStoryOrThrow(userStoryId);
        userStory.setStatus(status);
        return userStoryRepository.save(userStory);
    }



    public List<UserStory> getPrioritizedUserStories(Long backlogId) {
        return productBacklogRepository.findById(backlogId)
                .orElseThrow(() -> new EntityNotFoundException("Product Backlog non trouvé"))
                .getUserStories().stream()
                .sorted((us1, us2) -> Integer.compare(
                        us1.getPriority().getWeight(),
                        us2.getPriority().getWeight()
                ))
                .toList();
    }

    // ==== Query Methods ====
    public UserStory getUserStoryById(Long id) {
        return userStoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User Story non trouvée avec l'ID : " + id));
    }

    public List<UserStory> getAllUserStories() {
        return userStoryRepository.findAll();
    }

    private UserStory getUserStoryOrThrow(Long id) {
        return userStoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User Story non trouvée avec l'ID : " + id));
    }

    private void validateUserStory(UserStory userStory) {
        if (userStory.getRole() == null || userStory.getRole().isBlank()) {
            throw new IllegalArgumentException("Le champ 'role' est obligatoire");
        }
        if (userStory.getAction() == null || userStory.getAction().isBlank()) {
            throw new IllegalArgumentException("Le champ 'action' est obligatoire");
        }
    }

    private void updateFields(UserStory target, UserStory source) {
        target.setTitle(source.getTitle());
        target.setRole(source.getRole());
        target.setAction(source.getAction());
        target.setGoal(source.getGoal());
        target.setPriority(source.getPriority());
        target.setStatus(source.getStatus());
        target.setAcceptanceCriteria(source.getAcceptanceCriteria());
    }


}
