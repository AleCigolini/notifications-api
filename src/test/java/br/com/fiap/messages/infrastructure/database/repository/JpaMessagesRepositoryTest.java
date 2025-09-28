package br.com.fiap.messages.infrastructure.database.repository;

import br.com.fiap.messages.common.domain.entity.JpaMessagesEntity;
import br.com.fiap.messages.domain.MessageType;
import br.com.fiap.messages.domain.StatusType;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JpaMessagesRepositoryTest {

    private JpaMessagesRepository repository;
    private EntityManager entityManager;

    private final UUID testId = UUID.randomUUID();
    private final String testContent = "Test message content";
    private final OffsetDateTime testCreatedAt = OffsetDateTime.now();
    private final MessageType testType = MessageType.B;
    private final String testRecipient = "user@example.com";
    private final StatusType testStatus = StatusType.INFO;

    @BeforeEach
    void setUp() {
        repository = spy(new JpaMessagesRepository());
        entityManager = mock(EntityManager.class);
        doReturn(entityManager).when(repository).getEntityManager();
    }

    private JpaMessagesEntity createTestEntity() {
        JpaMessagesEntity entity = new JpaMessagesEntity();
        entity.setId(testId);
        entity.setContent(testContent);
        entity.setCreatedAt(testCreatedAt);
        entity.setType(testType);
        entity.setRecipient(testRecipient);
        entity.setStatus(testStatus);
        return entity;
    }

    @Nested
    class FindByIdTests {

        @Test
        void shouldFindEntityById() {
            JpaMessagesEntity expected = createTestEntity();
            doReturn(expected).when(repository).findById(testId);

            JpaMessagesEntity found = repository.findById(testId);

            assertNotNull(found);
            assertEquals(testId, found.getId());
            assertEquals(testContent, found.getContent());
        }

        @Test
        void shouldReturnNullWhenEntityNotFound() {
            doReturn(null).when(repository).findById(testId);

            JpaMessagesEntity found = repository.findById(testId);

            assertNull(found);
        }

        @Test
        void shouldFindOptionalEntityById() {
            JpaMessagesEntity expected = createTestEntity();
            doReturn(Optional.of(expected)).when(repository).findByIdOptional(testId);

            Optional<JpaMessagesEntity> found = repository.findByIdOptional(testId);

            assertTrue(found.isPresent());
            assertEquals(testId, found.get().getId());
        }

        @Test
        void shouldReturnEmptyOptionalWhenEntityNotFound() {
            doReturn(Optional.empty()).when(repository).findByIdOptional(testId);

            Optional<JpaMessagesEntity> found = repository.findByIdOptional(testId);

            assertFalse(found.isPresent());
        }
    }

    @Nested
    class PersistTests {

        @Test
        void shouldPersistEntity() {
            JpaMessagesEntity entity = createTestEntity();
            doNothing().when(repository).persist(entity);

            repository.persist(entity);

            verify(repository).persist(entity);
        }

        @Test
        void shouldPersistMultipleEntities() {

            JpaMessagesEntity entity1 = createTestEntity();
            JpaMessagesEntity entity2 = new JpaMessagesEntity();
            entity2.setId(UUID.randomUUID());
            entity2.setContent("Second test message");

            List<JpaMessagesEntity> entities = Arrays.asList(entity1, entity2);
            doNothing().when(repository).persist(entities);

            repository.persist(entities);

            verify(repository).persist(entities);
        }

        @Test
        void shouldPersistAndFlushEntity() {
            JpaMessagesEntity entity = createTestEntity();
            doNothing().when(repository).persistAndFlush(entity);

            repository.persistAndFlush(entity);

            verify(repository).persistAndFlush(entity);
        }
    }

    @Nested
    class DeleteTests {

        @Test
        void shouldDeleteEntityById() {
            doReturn(true).when(repository).deleteById(testId);

            boolean deleted = repository.deleteById(testId);

            assertTrue(deleted);
            verify(repository).deleteById(testId);
        }

        @Test
        void shouldReturnFalseWhenDeletingNonExistentEntity() {
            doReturn(false).when(repository).deleteById(testId);

            boolean deleted = repository.deleteById(testId);

            assertFalse(deleted);
            verify(repository).deleteById(testId);
        }

        @Test
        void shouldDeleteEntity() {
            JpaMessagesEntity entity = createTestEntity();
            doNothing().when(repository).delete(entity);

            repository.delete(entity);

            verify(repository).delete(entity);
        }
    }

    @Nested
    class ListAndCountTests {

        @Test
        void shouldListAllEntities() {
            JpaMessagesEntity entity1 = createTestEntity();
            JpaMessagesEntity entity2 = new JpaMessagesEntity();
            entity2.setId(UUID.randomUUID());

            List<JpaMessagesEntity> expectedEntities = Arrays.asList(entity1, entity2);
            doReturn(expectedEntities).when(repository).listAll();

            List<JpaMessagesEntity> entities = repository.listAll();

            assertEquals(2, entities.size());
            assertEquals(expectedEntities, entities);
        }

        @Test
        void shouldCountEntities() {
            doReturn(10L).when(repository).count();

            long count = repository.count();

            assertEquals(10L, count);
        }
    }

    @Nested
    class QueryTests {

        @Test
        void shouldFindEntitiesByType() {
            JpaMessagesEntity entity1 = createTestEntity();
            JpaMessagesEntity entity2 = createTestEntity();
            List<JpaMessagesEntity> expectedEntities = Arrays.asList(entity1, entity2);

            doReturn(expectedEntities).when(repository).list("type", MessageType.B);

            List<JpaMessagesEntity> entities = repository.list("type", MessageType.B);

            assertEquals(2, entities.size());
            assertEquals(expectedEntities, entities);
        }

        @Test
        void shouldFindEntitiesByRecipient() {
            JpaMessagesEntity entity = createTestEntity();
            List<JpaMessagesEntity> expectedEntities = List.of(entity);

            doReturn(expectedEntities).when(repository).list("recipient", testRecipient);

            List<JpaMessagesEntity> entities = repository.list("recipient", testRecipient);

            assertEquals(1, entities.size());
            assertEquals(entity.getId(), entities.get(0).getId());
            assertEquals(entity.getRecipient(), entities.get(0).getRecipient());
        }

        @Test
        void shouldFindEntitiesByChannel() {
            JpaMessagesEntity entity = createTestEntity();
            List<JpaMessagesEntity> expectedEntities = List.of(entity);

            doReturn(expectedEntities).when(repository).list("status", testStatus);

            List<JpaMessagesEntity> entities = repository.list("status", testStatus);

            assertEquals(1, entities.size());
            assertEquals(entity.getStatus(), entities.get(0).getStatus());
        }
    }
}
