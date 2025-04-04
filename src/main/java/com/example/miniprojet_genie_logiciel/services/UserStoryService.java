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
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserStoryService {

    private final UserStoryRepository userStoryRepository;
    private final EpicRepository epicRepository;
    private final ProductBacklogRepository productBacklogRepository;

    // ==== CRUD Operations ====
    public UserStory addUserStory( UserStory userStory) {
        return userStoryRepository.save(userStory);
    }

    public UserStory updateUserStory(Long id, UserStory updatedStory) {
        UserStory existing = getUserStoryById(id);
        existing.setTitle(updatedStory.getTitle());
        existing.setAction(updatedStory.getAction());
        existing.setRole(updatedStory.getRole());
        existing.setGoal(updatedStory.getGoal());
        return userStoryRepository.save(existing);

    }


    public UserStory updateUserStoryPriority(Long id, Priority priority) {
        UserStory existing = getUserStoryOrThrow(id);
        existing.setPriority(priority);
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
        UserStory userStory = userStoryRepository.findById(userStoryId)
                .orElseThrow(() -> new EntityNotFoundException("UserStory not found with id: " + userStoryId));
        Epic epic = epicRepository.findById(epicId)
                .orElseThrow(() -> new EntityNotFoundException("Epic not found with id: " + epicId));

        userStory.setEpic(epic);

        if (!epic.getUserStories().contains(userStory)) {
            epic.getUserStories().add(userStory);
        }

        return userStoryRepository.save(userStory);
    }

    public void unlinkUserStoryFromEpic(Long userStoryId, Long EpicId) {
        UserStory userStory = getUserStoryOrThrow(userStoryId);

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

}
