package com.example.miniprojet_genie_logiciel.serviceTest;

import com.example.miniprojet_genie_logiciel.entities.ProductBacklog;
import com.example.miniprojet_genie_logiciel.entities.Epic;
import com.example.miniprojet_genie_logiciel.entities.UserStory;
import com.example.miniprojet_genie_logiciel.repository.ProductBacklogRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductBacklogServiceTest {

    @Mock
    private ProductBacklogRepository productBacklogRepository;

    @InjectMocks
    private ProductBacklogService productBacklogService;

    private AutoCloseable closeable;

    @BeforeAll
    void initAll() {
        // Ouverture des mocks pour l'ensemble des tests
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterAll
    void tearDownAll() throws Exception {
        // Fermeture des ressources (mocks)
        closeable.close();
    }

    @BeforeEach
    void init() {
        // Initialisation ou réinitialisation avant chaque test (si besoin)
    }

    @AfterEach
    void tearDown() {
        // Nettoyage après chaque test (si besoin)
    }

    @Test
    void testSaveProductBacklog() {
        // Arrange
        ProductBacklog pb = new ProductBacklog();
        pb.setId(1L);

        when(productBacklogRepository.save(any(ProductBacklog.class))).thenReturn(pb);

        // Act
        ProductBacklog saved = productBacklogService.saveProductBacklog(pb);

        // Assert
        assertNotNull(saved);
        assertEquals(1L, saved.getId());
        verify(productBacklogRepository, times(1)).save(pb);
    }

    @Test
    void testFindAll() {
        // Arrange
        ProductBacklog pb1 = new ProductBacklog();
        pb1.setId(1L);
        ProductBacklog pb2 = new ProductBacklog();
        pb2.setId(2L);
        when(productBacklogRepository.findAll()).thenReturn(Arrays.asList(pb1, pb2));

        // Act
        List<ProductBacklog> backlogs = productBacklogService.findAll();

        // Assert
        assertEquals(2, backlogs.size());
        verify(productBacklogRepository, times(1)).findAll();
    }

    @Test
    void testFindByIdFound() {
        // Arrange
        ProductBacklog pb = new ProductBacklog();
        pb.setId(1L);
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(pb));

        // Act
        Optional<ProductBacklog> result = productBacklogService.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(productBacklogRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        // Arrange
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<ProductBacklog> result = productBacklogService.findById(1L);

        // Assert
        assertFalse(result.isPresent());
        verify(productBacklogRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteById() {
        // Act
        productBacklogService.deleteById(1L);

        // Assert
        verify(productBacklogRepository, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateProductBacklog() {
        // Arrange
        ProductBacklog pb = new ProductBacklog();
        pb.setId(1L);
        when(productBacklogRepository.save(any(ProductBacklog.class))).thenReturn(pb);

        // Act
        ProductBacklog updated = productBacklogService.updateProductBacklog(pb);

        // Assert
        assertNotNull(updated);
        assertEquals(1L, updated.getId());
        verify(productBacklogRepository, times(1)).save(pb);
    }

    @Test
    void testAddEpicToProductBacklog() {
        // Arrange
        ProductBacklog pb = new ProductBacklog();
        pb.setId(1L);
        // On s'assure que la liste d'epics est initialisée
        pb.setEpics(new ArrayList<>());
        Epic epic = new Epic();
        epic.setId(10L);

        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(pb));
        // La sauvegarde renvoie l'objet tel quel
        when(productBacklogRepository.save(any(ProductBacklog.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ProductBacklog updated = productBacklogService.addEpicToProductBacklog(1L, epic);

        // Assert
        assertTrue(updated.getEpics().contains(epic));
        verify(productBacklogRepository, times(1)).findById(1L);
        verify(productBacklogRepository, times(1)).save(pb);
    }

    @Test
    void testRemoveEpicFromProductBacklog() {
        // Arrange
        Epic epic = new Epic();
        epic.setId(10L);
        ProductBacklog pb = new ProductBacklog();
        pb.setId(1L);
        // Initialisation de la liste avec un epic présent
        pb.setEpics(new ArrayList<>(Collections.singletonList(epic)));

        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(pb));
        when(productBacklogRepository.save(any(ProductBacklog.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ProductBacklog updated = productBacklogService.removeEpicFromProductBacklog(1L, epic);

        // Assert
        assertFalse(updated.getEpics().contains(epic));
        verify(productBacklogRepository, times(1)).findById(1L);
        verify(productBacklogRepository, times(1)).save(pb);
    }

    @Test
    void testAddUserStoryToProductBacklog() {
        // Arrange
        ProductBacklog pb = new ProductBacklog();
        pb.setId(1L);
        pb.setUserStories(new ArrayList<>());
        UserStory userStory = new UserStory();
        userStory.setId(100L);

        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(pb));
        when(productBacklogRepository.save(any(ProductBacklog.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ProductBacklog updated = productBacklogService.addUserStoryToProductBacklog(1L, userStory);

        // Assert
        assertTrue(updated.getUserStories().contains(userStory));
        verify(productBacklogRepository, times(1)).findById(1L);
        verify(productBacklogRepository, times(1)).save(pb);
    }

    @Test
    void testRemoveUserStoryFromProductBacklog() {
        // Arrange
        UserStory userStory = new UserStory();
        userStory.setId(100L);
        ProductBacklog pb = new ProductBacklog();
        pb.setId(1L);
        pb.setUserStories(new ArrayList<>(Collections.singletonList(userStory)));

        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(pb));
        when(productBacklogRepository.save(any(ProductBacklog.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ProductBacklog updated = productBacklogService.removeUserStoryFromProductBacklog(1L, userStory);

        // Assert
        assertFalse(updated.getUserStories().contains(userStory));
        verify(productBacklogRepository, times(1)).findById(1L);
        verify(productBacklogRepository, times(1)).save(pb);
    }

    @Test
    void testPrioritizeUserStoriesMoscow() {
        // Arrange
        // Création de plusieurs user stories avec des priorités différentes
        UserStory us1 = new UserStory();
        us1.setId(1L);
        us1.setPriority("Must Have");   // valeur 1
        UserStory us2 = new UserStory();
        us2.setId(2L);
        us2.setPriority("Should Have"); // valeur 2
        UserStory us3 = new UserStory();
        us3.setId(3L);
        us3.setPriority("Could Have");  // valeur 3
        UserStory us4 = new UserStory();
        us4.setId(4L);
        us4.setPriority(null);          // valeur MAX

        ProductBacklog pb = new ProductBacklog();
        pb.setId(1L);
        // On mélange les user stories
        pb.setUserStories(Arrays.asList(us3, us1, us2, us4));

        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(pb));

        // Act
        List<UserStory> prioritized = productBacklogService.prioritizeUserStoriesMoscow(1L);

        // Assert
        // Tri attendu : Must Have (us1), Should Have (us2), Could Have (us3), puis user story sans priorité (us4)
        assertEquals(us1, prioritized.get(0));
        assertEquals(us2, prioritized.get(1));
        assertEquals(us3, prioritized.get(2));
        assertEquals(us4, prioritized.get(3));

        verify(productBacklogRepository, times(1)).findById(1L);
    }

    @Test
    void testAddEpicToProductBacklogNotFound() {
        // Arrange
        Epic epic = new Epic();
        epic.setId(10L);
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> productBacklogService.addEpicToProductBacklog(1L, epic));
        verify(productBacklogRepository, times(1)).findById(1L);
    }
}
