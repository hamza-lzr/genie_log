package com.example.miniprojet_genie_logiciel.services;

import com.example.miniprojet_genie_logiciel.entities.ProductBacklog;
import com.example.miniprojet_genie_logiciel.entities.Epic;
import com.example.miniprojet_genie_logiciel.entities.UserStory;
import com.example.miniprojet_genie_logiciel.repository.EpicRepository;
import com.example.miniprojet_genie_logiciel.repository.ProductBacklogRepository;
import com.example.miniprojet_genie_logiciel.repository.UserStoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductBacklogService {

    private final ProductBacklogRepository productbacklogrepository;
    private final EpicRepository epicRepository;
    private final UserStoryRepository userStoryRepository;

    // CRUD de base
    public ProductBacklog saveProductBacklog(ProductBacklog productbacklog) {
        return productbacklogrepository.save(productbacklog);
    }

    public List<ProductBacklog> findAll() {
        return productbacklogrepository.findAll();
    }

    public Optional<ProductBacklog> findById(Long id) {
        return productbacklogrepository.findById(id);
    }

    public void deleteById(Long id) {
        productbacklogrepository.deleteById(id);
    }

    // Mise à jour d'un ProductBacklog existant
    public ProductBacklog updateProductBacklog(ProductBacklog Oldproductbacklog, Long backlogId) {
        Optional<ProductBacklog> PBToUpdate = productbacklogrepository.findById(backlogId);
        if (PBToUpdate.isPresent()) {
            ProductBacklog PBToUpdateObj = PBToUpdate.get();
            PBToUpdateObj.setName(Oldproductbacklog.getName());

        }

        return productbacklogrepository.save(Oldproductbacklog);
    }

    // Ajout d'un Epic au ProductBacklog
    public ProductBacklog addEpicToProductBacklog(Long backlogId, Long epicId) {
        ProductBacklog pb = productbacklogrepository.findById(backlogId)
                .orElseThrow(() -> new EntityNotFoundException("ProductBacklog not found with id: " + backlogId));
        Epic ep = epicRepository.findById(epicId)
                        .orElseThrow(() -> new EntityNotFoundException("Epic not found with id: " + epicId));

        pb.getEpics().add(ep);

        return productbacklogrepository.save(pb);
    }

    // Suppression d'un Epic du ProductBacklog
    public ProductBacklog removeEpicFromProductBacklog(Long backlogId, Long epicId) {
        ProductBacklog pb = productbacklogrepository.findById(backlogId)
                .orElseThrow(() -> new EntityNotFoundException("ProductBacklog not found with id: " + backlogId));
        Epic ep = epicRepository.findById(epicId)
                .orElseThrow(() -> new EntityNotFoundException("Epic not found with id: " + epicId));
        pb.getEpics().remove(ep);
        return productbacklogrepository.save(pb);
    }

    // Ajout d'une UserStory au ProductBacklog
    public ProductBacklog addUserStoryToProductBacklog(Long backlogId, Long userStoryId) {
        ProductBacklog pb = productbacklogrepository.findById(backlogId)
                .orElseThrow(() -> new EntityNotFoundException("ProductBacklog not found with id: " + backlogId));
        UserStory us = userStoryRepository.findById(userStoryId)
                .orElseThrow(() -> new EntityNotFoundException(" User Story not found with id: "+ userStoryId));
        pb.getUserStories().add(us);
        return productbacklogrepository.save(pb);
    }

    // Suppression d'une UserStory du ProductBacklog
    public ProductBacklog removeUserStoryFromProductBacklog(Long backlogId, Long userStoryId) {
        ProductBacklog pb = productbacklogrepository.findById(backlogId)
                .orElseThrow(() -> new EntityNotFoundException("ProductBacklog not found with id: " + backlogId));
        UserStory us = userStoryRepository.findById(userStoryId)
                        .orElseThrow(() -> new EntityNotFoundException(" User Story not found with id: "+ userStoryId));
        pb.getUserStories().remove(us);
        return productbacklogrepository.save(pb);
    }

    public List<UserStory> prioritizeUserStoriesMoscow(Long backlogId) {
        ProductBacklog pb = productbacklogrepository.findById(backlogId)
                .orElseThrow(() -> new EntityNotFoundException("ProductBacklog not found with id: " + backlogId));

        return pb.getUserStories().stream()
                .sorted(Comparator.comparingInt((UserStory us) -> us.getPriority().getWeight()).reversed())
                .collect(Collectors.toList());
    }


}
