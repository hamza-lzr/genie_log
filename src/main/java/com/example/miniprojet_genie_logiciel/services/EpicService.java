package com.example.miniprojet_genie_logiciel.services;


import com.example.miniprojet_genie_logiciel.entities.Epic;
import com.example.miniprojet_genie_logiciel.repositories.EpicRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EpicService {

    private final EpicRepository epicRepository;
    public EpicService(EpicRepository epicRepository) {
        this.epicRepository = epicRepository;
    }

    public Epic getEpicById(long id) {
        return epicRepository.findById(id).orElse(null);
    }
    public List<Epic> getAllEpics() {
        return epicRepository.findAll();
    }
    public Epic createEpic(Epic epic) {
        return epicRepository.save(epic);
    }
    public void deleteEpicById(long id) {
         epicRepository.deleteEpicById(id);
    }

    public Epic updateEpic(Epic epic, long id) {
        Optional<Epic> epicOptional = epicRepository.findById(id);
        if (epicOptional.isPresent()) {
            Epic epicToUpdate = epicOptional.get();
            epicToUpdate.setDescription(epic.getDescription());
            epicToUpdate.setUserStories(epic.getUserStories());
            epicToUpdate.setTitle(epic.getTitle());
            return epicRepository.save(epicToUpdate);
        }
        return null;

    }
}
