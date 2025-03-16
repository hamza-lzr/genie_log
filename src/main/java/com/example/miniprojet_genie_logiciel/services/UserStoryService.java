package com.example.miniprojet_genie_logiciel.services;


import com.example.miniprojet_genie_logiciel.entities.UserStory;
import com.example.miniprojet_genie_logiciel.repositories.UserStoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserStoryService {

    private final UserStoryRepository userStoryRepository;

    public UserStoryService(UserStoryRepository userStoryRepository) {
        this.userStoryRepository = userStoryRepository;
    }

    public Optional<UserStory> getUserStoryById(Long id) {
        return userStoryRepository.findById(id);
    }

    public List<UserStory> getAllUserStory() {
        return userStoryRepository.findAll();
    }

    public UserStory createUserStory(UserStory userStory) {
        return userStoryRepository.save(userStory);
    }

    public void deleteUserStoryById(Long id) {
        userStoryRepository.deleteById(id);
    }

    public UserStory updateUserStory(UserStory userStory, Long id) {
        Optional<UserStory> userStory1 = getUserStoryById(id);
        if (userStory1.isPresent()) {
            UserStory userStoryexists = userStory1.get();
            userStoryexists.setTitle(userStory.getTitle());
            userStoryexists.setRole(userStory.getRole());
            userStoryexists.setAction(userStory.getAction());
            userStoryexists.setStatus(userStory.getStatus());
            userStoryexists.setValue(userStory.getValue());
            userStoryexists.setPriority(userStory.getPriority());
            return userStoryRepository.save(userStoryexists);
        }
        else return null;
    }

}
