package com.example.miniprojet_genie_logiciel.services;

import com.example.miniprojet_genie_logiciel.dto.*;
import com.example.miniprojet_genie_logiciel.entities.*;
import com.example.miniprojet_genie_logiciel.entities.Status;
import com.example.miniprojet_genie_logiciel.mapper.EpicMapper;
import com.example.miniprojet_genie_logiciel.mapper.ProductBacklogMapper;
import com.example.miniprojet_genie_logiciel.mapper.UserStoryMapper;
import com.example.miniprojet_genie_logiciel.repository.EpicRepository;
import com.example.miniprojet_genie_logiciel.repository.ProductBacklogRepository;
import com.example.miniprojet_genie_logiciel.repository.UserStoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductBacklogService {

    private final ProductBacklogRepository backlogRepository;
    private final EpicRepository epicRepository;
    private final UserStoryRepository userStoryRepository;
    private final ProductBacklogMapper backlogMapper;
    private final EpicMapper epicMapper;
    private final UserStoryMapper userStoryMapper;

    // === CRUD Product Backlog ===

    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER')")
    public ProductBacklogDTO createBacklog(ProductBacklogDTO dto) {
        ProductBacklog backlog = backlogMapper.toEntity(dto);
        return backlogMapper.toDto(backlogRepository.save(backlog));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER', 'SCRUM_MASTER')")
    public List<ProductBacklogDTO> findAll() {
        return backlogRepository.findAll().stream()
                .map(backlogMapper::toDto)
                .toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER', 'SCRUM_MASTER')")
    public Optional<ProductBacklogDTO> findById(Long id) {
        return backlogRepository.findById(id)
                .map(backlogMapper::toDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteById(Long id) {
        backlogRepository.deleteById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER')")
    public ProductBacklogDTO updateBacklog(Long id, ProductBacklogDTO dto) {
        ProductBacklog backlog = backlogRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Backlog not found: " + id));
        backlog.setName(dto.getName());
        backlog.setDescription(dto.getDescription());
        return backlogMapper.toDto(backlogRepository.save(backlog));
    }

    // === EPICs ===

    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER')")
    public EpicDTO createEpic(Long backlogId, CreateEpicDTO dto) {
        ProductBacklog backlog = getBacklog(backlogId);
        Epic epic = epicMapper.fromCreateDto(dto);
        epic.setProductBacklog(backlog);
        return epicMapper.toDto(epicRepository.save(epic));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER')")
    public void deleteEpic(Long backlogId, Long epicId) {
        Epic epic = getEpic(epicId);
        if (!epic.getProductBacklog().getId().equals(backlogId)) {
            throw new IllegalStateException("Epic doesn't belong to backlog " + backlogId);
        }
        epicRepository.delete(epic);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER')")
    public UserStoryDTO createUserStory(Long backlogId, CreateUserStoryDTO dto) {
        ProductBacklog backlog = getBacklog(backlogId);

        Epic epic = null;
        if (dto.getEpicId() != null) {
            epic = epicRepository.findById(dto.getEpicId())
                    .orElseThrow(() -> new EntityNotFoundException("Epic not found with id: " + dto.getEpicId()));
        }

        UserStory story = userStoryMapper.fromCreateDto(dto, epic, backlog);
        return userStoryMapper.toDto(userStoryRepository.save(story));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER')")
    public void deleteUserStory(Long backlogId, Long storyId) {
        UserStory story = getUserStory(storyId);

        if (!story.getProductBacklog().getId().equals(backlogId)) {
            throw new IllegalStateException("UserStory doesn't belong to backlog " + backlogId);
        }

        userStoryRepository.delete(story);
    }


    // === MOSCOW Prioritization ===

    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER', 'SCRUM_MASTER')")
    public List<UserStoryDTO> prioritizeUserStories(Long backlogId) {
        ProductBacklog backlog = getBacklog(backlogId);
        return backlog.getUserStories().stream()
                .sorted(Comparator.comparingInt(us -> ((UserStory) us).getPriority().getWeight()).reversed())
                .map(userStoryMapper::toDto)
                .collect(Collectors.toList());
    }

    // === Filtering ===

    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER', 'SCRUM_MASTER')")
    public List<UserStoryDTO> filterUserStories(Long backlogId, Status status, PriorityDTO priority, String keyword) {
        ProductBacklog backlog = getBacklog(backlogId);

        return backlog.getUserStories().stream()
                .filter(us -> status == null || us.getStatus() == status)
                .filter(us -> priority == null || us.getPriority().getLabel().equalsIgnoreCase(priority.getName()))
                .filter(us -> keyword == null || keyword.isBlank() || containsKeyword(us, keyword))
                .map(userStoryMapper::toDto)
                .collect(Collectors.toList());
    }

    // === Helpers ===

    private boolean containsKeyword(UserStory us, String keyword) {
        String k = keyword.toLowerCase();
        return us.getTitle().toLowerCase().contains(k)
                || us.getAcceptanceCriteria().toLowerCase().contains(k)
                || us.getStatus().toString().toLowerCase().contains(k)
                || us.getPriority().getLabel().toLowerCase().contains(k)
                || us.getTitle().toLowerCase().contains(k)
                ;
    }

    private ProductBacklog getBacklog(Long id) {
        return backlogRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Backlog not found: " + id));
    }

    private Epic getEpic(Long id) {
        return epicRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Epic not found: " + id));
    }

    private UserStory getUserStory(Long id) {
        return userStoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User Story not found: " + id));
    }
}
