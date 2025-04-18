package com.example.miniprojet_genie_logiciel.services;

import com.example.miniprojet_genie_logiciel.dto.*;
import com.example.miniprojet_genie_logiciel.entities.*;
import com.example.miniprojet_genie_logiciel.mapper.EpicMapper;
import com.example.miniprojet_genie_logiciel.mapper.ProductBacklogMapper;
import com.example.miniprojet_genie_logiciel.mapper.UserStoryMapper;
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
    private final ProductBacklogMapper productBacklogMapper;
    private final EpicMapper epicMapper;
    private final UserStoryMapper userStoryMapper;

    public ProductBacklogDTO saveProductBacklog(ProductBacklogDTO productBacklogDTO) {
        ProductBacklog productBacklog = productBacklogMapper.toEntity(productBacklogDTO);
        ProductBacklog savedProductBacklog = productbacklogrepository.save(productBacklog);
        return productBacklogMapper.toDto(savedProductBacklog);
    }

    public List<ProductBacklogDTO> findAll() {
        List<ProductBacklog> productBacklogs = productbacklogrepository.findAll();
        return productBacklogs.stream()
                .map(productBacklogMapper::toDto)
                .toList();
    }

    public Optional<ProductBacklogDTO> findById(Long id) {
        return productbacklogrepository.findById(id)
                .map(productBacklogMapper::toDto);
    }

    public void deleteById(Long id) {
        productbacklogrepository.deleteById(id);
    }

    public ProductBacklogDTO updateProductBacklog(ProductBacklogDTO oldProductBacklogDTO, Long backlogId) {
        Optional<ProductBacklog> PBToUpdate = productbacklogrepository.findById(backlogId);
        if (PBToUpdate.isPresent()) {
            ProductBacklog PBToUpdateObj = PBToUpdate.get();
            PBToUpdateObj.setName(oldProductBacklogDTO.getName());
            ProductBacklog savedProductBacklog = productbacklogrepository.save(PBToUpdateObj);
            return productBacklogMapper.toDto(savedProductBacklog);
        }
        throw new EntityNotFoundException("ProductBacklog not found with id: " + backlogId);
    }

    // Ajout d'un Epic au ProductBacklog
    public ProductBacklogDTO addEpicToProductBacklog(Long backlogId, Long epicId) {
        ProductBacklog pb = productbacklogrepository.findById(backlogId)
                .orElseThrow(() -> new EntityNotFoundException("ProductBacklog not found with id: " + backlogId));
        Epic ep = epicRepository.findById(epicId)
                        .orElseThrow(() -> new EntityNotFoundException("Epic not found with id: " + epicId));

        pb.getEpics().add(ep);
        ProductBacklog savedPb = productbacklogrepository.save(pb);
        return productBacklogMapper.toDto(savedPb);
    }

    // Suppression d'un Epic du ProductBacklog
    public ProductBacklogDTO removeEpicFromProductBacklog(Long backlogId, Long epicId) {
        ProductBacklog pb = productbacklogrepository.findById(backlogId)
                .orElseThrow(() -> new EntityNotFoundException("ProductBacklog not found with id: " + backlogId));
        Epic ep = epicRepository.findById(epicId)
                .orElseThrow(() -> new EntityNotFoundException("Epic not found with id: " + epicId));
        pb.getEpics().remove(ep);
        ProductBacklog savedPb = productbacklogrepository.save(pb);
        return productBacklogMapper.toDto(savedPb);
    }

    // Ajout d'une UserStory au ProductBacklog
    public ProductBacklogDTO addUserStoryToProductBacklog(Long backlogId, Long userStoryId) {
        ProductBacklog pb = productbacklogrepository.findById(backlogId)
                .orElseThrow(() -> new EntityNotFoundException("ProductBacklog not found with id: " + backlogId));
        UserStory us = userStoryRepository.findById(userStoryId)
                .orElseThrow(() -> new EntityNotFoundException(" User Story not found with id: "+ userStoryId));
        pb.getUserStories().add(us);
        us.setProductBacklog(pb);
        ProductBacklog savedPb = productbacklogrepository.save(pb);
        return productBacklogMapper.toDto(savedPb);
    }


    // Suppression d'une UserStory du ProductBacklog
    public ProductBacklogDTO removeUserStoryFromProductBacklog(Long backlogId, Long userStoryId) {
        ProductBacklog pb = productbacklogrepository.findById(backlogId)
                .orElseThrow(() -> new EntityNotFoundException("ProductBacklog not found with id: " + backlogId));
        UserStory us = userStoryRepository.findById(userStoryId)
                        .orElseThrow(() -> new EntityNotFoundException(" User Story not found with id: "+ userStoryId));
        pb.getUserStories().remove(us);
        ProductBacklog savedPb = productbacklogrepository.save(pb);
        return productBacklogMapper.toDto(savedPb);
    }
    //classer les User Stories par ordre de priorite
    public List<UserStoryDTO> prioritizeUserStoriesMoscow(Long backlogId) {
        ProductBacklog pb = productbacklogrepository.findById(backlogId)
                .orElseThrow(() -> new EntityNotFoundException("ProductBacklog not found with id: " + backlogId));

        return pb.getUserStories().stream()
                .sorted(Comparator.comparingInt((UserStory us) -> us.getPriority().getWeight()).reversed())
                .map(userStoryMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<UserStoryDTO> filterUserStories(Long backlogId,
                                             StatusDTO statusFilter,
                                             PriorityDTO priorityFilter,
                                             String keyword) {
        ProductBacklog pb = productbacklogrepository.findById(backlogId)
                .orElseThrow(() -> new EntityNotFoundException("ProductBacklog not found with id: " + backlogId));

        return pb.getUserStories().stream()
                .filter(us -> statusFilter == null || us.getStatus().toString().equals(statusFilter.toString()))
                .filter(us -> priorityFilter == null || us.getPriority().getLabel().equals(priorityFilter.getName()))
                .filter(us -> keyword == null || keyword.isBlank()
                        || us.getTitle().toLowerCase().contains(keyword.toLowerCase())
                        || us.getAcceptanceCriteria().toLowerCase().contains(keyword.toLowerCase())
                        || us.getAction().toLowerCase().contains(keyword.toLowerCase())
                        || us.getRole().toLowerCase().contains(keyword.toLowerCase())
                        || us.getGoal().toLowerCase().contains(keyword.toLowerCase())
                )
                .map(userStoryMapper::toDto)
                .collect(Collectors.toList());
    }





}


