package com.example.miniprojet_genie_logiciel.services;

import com.example.miniprojet_genie_logiciel.entities.ProductBacklog;
import com.example.miniprojet_genie_logiciel.entities.Epic;
import com.example.miniprojet_genie_logiciel.entities.UserStory;
import com.example.miniprojet_genie_logiciel.repository.ProductBacklogRepository;
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
    public ProductBacklog updateProductBacklog(ProductBacklog productbacklog) {
        // Si l'id est présent, save() effectue bien une mise à jour
        return productbacklogrepository.save(productbacklog);
    }

    // Ajout d'un Epic au ProductBacklog
    public ProductBacklog addEpicToProductBacklog(Long backlogId, Epic epic) {
        ProductBacklog pb = productbacklogrepository.findById(backlogId)
                .orElseThrow(() -> new EntityNotFoundException("ProductBacklog not found with id: " + backlogId));
        pb.getEpics().add(epic);
        return productbacklogrepository.save(pb);
    }

    // Suppression d'un Epic du ProductBacklog
    public ProductBacklog removeEpicFromProductBacklog(Long backlogId, Epic epic) {
        ProductBacklog pb = productbacklogrepository.findById(backlogId)
                .orElseThrow(() -> new EntityNotFoundException("ProductBacklog not found with id: " + backlogId));
        pb.getEpics().remove(epic);
        return productbacklogrepository.save(pb);
    }

    // Ajout d'une UserStory au ProductBacklog
    public ProductBacklog addUserStoryToProductBacklog(Long backlogId, UserStory userStory) {
        ProductBacklog pb = productbacklogrepository.findById(backlogId)
                .orElseThrow(() -> new EntityNotFoundException("ProductBacklog not found with id: " + backlogId));
        pb.getUserStories().add(userStory);
        return productbacklogrepository.save(pb);
    }

    // Suppression d'une UserStory du ProductBacklog
    public ProductBacklog removeUserStoryFromProductBacklog(Long backlogId, UserStory userStory) {
        ProductBacklog pb = productbacklogrepository.findById(backlogId)
                .orElseThrow(() -> new EntityNotFoundException("ProductBacklog not found with id: " + backlogId));
        pb.getUserStories().remove(userStory);
        return productbacklogrepository.save(pb);
    }

    //priorisation des userstories
    public List<UserStory> prioritizeUserStoriesMoscow(Long backlogId) {
        ProductBacklog pb = productbacklogrepository.findById(backlogId)
                .orElseThrow(() -> new EntityNotFoundException("ProductBacklog not found with id: " + backlogId));
        return pb.getUserStories().stream()
                .sorted(Comparator.comparingInt(us -> moscowValue(us.getPriority())))
                .collect(Collectors.toList());
    }

    private int moscowValue(String priority) {
        if (priority == null) {
            return Integer.MAX_VALUE;
        }
        switch (priority.toLowerCase()) {
            case "must have":
                return 1;
            case "should have":
                return 2;
            case "could have":
                return 3;
            case "won't have":
                return 4;
            default:
                return Integer.MAX_VALUE;
        }
    }


}
