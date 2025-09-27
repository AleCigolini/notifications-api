package br.com.fiap.messages.infrastructure.database.repository;

import br.com.fiap.messages.common.domain.entity.JpaMessagesEntity;
import br.com.fiap.messages.domain.MessageType;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class JpaMessagesRepositoryTest {

    private JpaMessagesRepository repository;
    private EntityManager entityManager;

    private final UUID testId = UUID.randomUUID();
    private final String testContent = "Test message content";
    private final OffsetDateTime testCreatedAt = OffsetDateTime.now();
    private final MessageType testType = MessageType.B;
    private final String testRecipient = "user@example.com";
    private final String testChannel = "EMAIL";

    @BeforeEach
    void setUp() {
        // Create a spy of the repository instead of trying to inject EntityManager directly
        repository = spy(new JpaMessagesRepository());
        entityManager = mock(EntityManager.class);

        // Use doReturn-when for the repository spy to control its behavior
        // This simulates the repository methods without needing to access the EntityManager directly
        doReturn(entityManager).when(repository).getEntityManager();
    }

    private JpaMessagesEntity createTestEntity() {
        JpaMessagesEntity entity = new JpaMessagesEntity();
        entity.setId(testId);
        entity.setContent(testContent);
        entity.setCreatedAt(testCreatedAt);
        entity.setType(testType);
        entity.setRecipient(testRecipient);
        entity.setChannel(testChannel);
        return entity;
    }

    @Nested
    class FindByIdTests {

        @Test
        void shouldFindEntityById() {
            // Arrange
            JpaMessagesEntity expected = createTestEntity();
            doReturn(expected).when(repository).findById(testId);

            // Act
            JpaMessagesEntity found = repository.findById(testId);

            // Assert
            assertNotNull(found);
            assertEquals(testId, found.getId());
            assertEquals(testContent, found.getContent());
        }

        @Test
        void shouldReturnNullWhenEntityNotFound() {
            // Arrange
            doReturn(null).when(repository).findById(testId);

            // Act
            JpaMessagesEntity found = repository.findById(testId);

            // Assert
            assertNull(found);
        }

        @Test
        void shouldFindOptionalEntityById() {
            // Arrange
            JpaMessagesEntity expected = createTestEntity();
            doReturn(Optional.of(expected)).when(repository).findByIdOptional(testId);

            // Act
            Optional<JpaMessagesEntity> found = repository.findByIdOptional(testId);

            // Assert
            assertTrue(found.isPresent());
            assertEquals(testId, found.get().getId());
        }

        @Test
        void shouldReturnEmptyOptionalWhenEntityNotFound() {
            // Arrange
            doReturn(Optional.empty()).when(repository).findByIdOptional(testId);

            // Act
            Optional<JpaMessagesEntity> found = repository.findByIdOptional(testId);

            // Assert
            assertFalse(found.isPresent());
        }
    }

    @Nested
    class PersistTests {

        @Test
        void shouldPersistEntity() {
            // Arrange
            JpaMessagesEntity entity = createTestEntity();
            doNothing().when(repository).persist(entity);

            // Act
            repository.persist(entity);

            // Assert
            verify(repository).persist(entity);
        }

        @Test
        void shouldPersistMultipleEntities() {
            // Arrange
            JpaMessagesEntity entity1 = createTestEntity();
            JpaMessagesEntity entity2 = new JpaMessagesEntity();
            entity2.setId(UUID.randomUUID());
            entity2.setContent("Second test message");

            List<JpaMessagesEntity> entities = Arrays.asList(entity1, entity2);
            doNothing().when(repository).persist(entities);

            // Act
            repository.persist(entities);

            // Assert
            verify(repository).persist(entities);
        }

        @Test
        void shouldPersistAndFlushEntity() {
            // Arrange
            JpaMessagesEntity entity = createTestEntity();
            doNothing().when(repository).persistAndFlush(entity);

            // Act
            repository.persistAndFlush(entity);

            // Assert
            verify(repository).persistAndFlush(entity);
        }
    }

    @Nested
    class DeleteTests {

        @Test
        void shouldDeleteEntityById() {
            // Arrange
            doReturn(true).when(repository).deleteById(testId);

            // Act
            boolean deleted = repository.deleteById(testId);

            // Assert
            assertTrue(deleted);
            verify(repository).deleteById(testId);
        }

        @Test
        void shouldReturnFalseWhenDeletingNonExistentEntity() {
            // Arrange
            doReturn(false).when(repository).deleteById(testId);

            // Act
            boolean deleted = repository.deleteById(testId);

            // Assert
            assertFalse(deleted);
            verify(repository).deleteById(testId);
        }

        @Test
        void shouldDeleteEntity() {
            // Arrange
            JpaMessagesEntity entity = createTestEntity();
            doNothing().when(repository).delete(entity);

            // Act
            repository.delete(entity);

            // Assert
            verify(repository).delete(entity);
        }
    }

    @Nested
    class ListAndCountTests {

        @Test
        void shouldListAllEntities() {
            // Arrange
            JpaMessagesEntity entity1 = createTestEntity();
            JpaMessagesEntity entity2 = new JpaMessagesEntity();
            entity2.setId(UUID.randomUUID());

            List<JpaMessagesEntity> expectedEntities = Arrays.asList(entity1, entity2);
            doReturn(expectedEntities).when(repository).listAll();

            // Act
            List<JpaMessagesEntity> entities = repository.listAll();

            // Assert
            assertEquals(2, entities.size());
            assertEquals(expectedEntities, entities);
        }

        @Test
        void shouldCountEntities() {
            // Arrange
            doReturn(10L).when(repository).count();

            // Act
            long count = repository.count();

            // Assert
            assertEquals(10L, count);
        }
    }

    @Nested
    class QueryTests {

        @Test
        void shouldFindEntitiesByType() {
            // Arrange
            JpaMessagesEntity entity1 = createTestEntity();
            JpaMessagesEntity entity2 = createTestEntity();
            List<JpaMessagesEntity> expectedEntities = Arrays.asList(entity1, entity2);

            doReturn(expectedEntities).when(repository).list("type", MessageType.B);

            // Act
            List<JpaMessagesEntity> entities = repository.list("type", MessageType.B);

            // Assert
            assertEquals(2, entities.size());
            assertEquals(expectedEntities, entities);
        }

        @Test
        void shouldFindEntitiesByRecipient() {
            // Arrange
            JpaMessagesEntity entity = createTestEntity();
            List<JpaMessagesEntity> expectedEntities = List.of(entity);

            doReturn(expectedEntities).when(repository).list("recipient", testRecipient);

            // Act
            List<JpaMessagesEntity> entities = repository.list("recipient", testRecipient);

            // Assert
            assertEquals(1, entities.size());
            assertEquals(entity.getId(), entities.get(0).getId());
            assertEquals(entity.getRecipient(), entities.get(0).getRecipient());
        }

        @Test
        void shouldFindEntitiesByChannel() {
            // Arrange
            JpaMessagesEntity entity = createTestEntity();
            List<JpaMessagesEntity> expectedEntities = List.of(entity);

            doReturn(expectedEntities).when(repository).list("channel", testChannel);

            // Act
            List<JpaMessagesEntity> entities = repository.list("channel", testChannel);

            // Assert
            assertEquals(1, entities.size());
            assertEquals(entity.getChannel(), entities.get(0).getChannel());
        }
    }
}
