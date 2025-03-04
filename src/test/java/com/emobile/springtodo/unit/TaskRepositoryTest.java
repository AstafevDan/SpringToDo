package com.emobile.springtodo.unit;

import com.emobile.springtodo.model.Task;
import com.emobile.springtodo.model.enums.TaskPriority;
import com.emobile.springtodo.model.enums.TaskStatus;
import com.emobile.springtodo.repository.TaskRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskRepositoryTest {

    private static final String ROW_COUNT_SQL = """
            SELECT COUNT(*) FROM tasks;
            """;

    private static final String FIND_ALL_BY_PAGE = """
            SELECT t.id, t.title, t.description, t.status, t.priority, t.created_at, t.last_modified_at
            FROM tasks AS t
            LIMIT :limit OFFSET :offset
            """;

    private static final String FIND_TASK_BY_ID = """
            SELECT t.id, t.title, t.description, t.status, t.priority, t.created_at, t.last_modified_at
            FROM tasks AS t
            WHERE id = :id
            """;

    private static final String SAVE_TASK = """
            INSERT INTO tasks (title, description, status, priority)
            VALUES (:title, :description, :status, :priority)
            RETURNING *
            """;

    private static final String UPDATE_TASK = """
            UPDATE tasks
            SET title = :title, description = :description, status = :status, priority = :priority
            WHERE id = :id
            """;

    private static final String DELETE_TASK = """
            DELETE FROM tasks
            WHERE id = :id
            """;

    private static final String UPDATE_TASK_PRIORITY = """
            UPDATE tasks
            SET priority = :priority
            WHERE id = :id
            """;

    private static final String UPDATE_TASK_STATUS = """
            UPDATE tasks
            SET status = :status
            WHERE id = :id
            """;

    private static final Long TASK_ID = 1L;

    private Task expectedTask1;
    private Task expectedTask2;

    @Mock
    private NamedParameterJdbcTemplate jdbcTemplate;

    @InjectMocks
    private TaskRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        expectedTask1 = Task.builder()
                .id(1L)
                .title("test")
                .description("test")
                .status(TaskStatus.PENDING)
                .priority(TaskPriority.LOW)
                .createdAt(Instant.now())
                .lastModifiedAt(Instant.now())
                .build();
        expectedTask2 = Task.builder()
                .id(2L)
                .title("test")
                .description("test")
                .status(TaskStatus.PENDING)
                .priority(TaskPriority.LOW)
                .createdAt(Instant.now())
                .lastModifiedAt(Instant.now())
                .build();
    }

    @Test
    void findAllByPageShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);

        when(jdbcTemplate.queryForObject(eq(ROW_COUNT_SQL),
                any(MapSqlParameterSource.class),
                eq(Integer.class))).thenReturn(2);
        when(jdbcTemplate.query(eq(FIND_ALL_BY_PAGE),
                anyMap(),
                any(RowMapper.class))).thenReturn(List.of(expectedTask1, expectedTask2));

        Page<Task> tasks = repository.findAllByPage(pageable);

        assertEquals(2, tasks.getTotalElements());
        assertEquals(2, tasks.getContent().size());
        assertEquals(expectedTask1, tasks.getContent().get(0));
        assertEquals(expectedTask2, tasks.getContent().get(1));
    }

    @Test
    void findTaskByIdShouldReturnTask() {
        when(jdbcTemplate.queryForObject(eq(FIND_TASK_BY_ID),
                anyMap(),
                any(RowMapper.class))).thenReturn(expectedTask1);

        Optional<Task> task = repository.findById(TASK_ID);

        assertTrue(task.isPresent());
        assertEquals(expectedTask1, task.get());
    }

    @Test
    void findNonExistingTaskShouldReturnOptionalEmpty() {
        Long nonExistingTaskId = 10L;

        when(jdbcTemplate.queryForObject(eq(FIND_TASK_BY_ID),
                anyMap(),
                any(RowMapper.class))).thenThrow(new EmptyResultDataAccessException(1));

        Optional<Task> task = repository.findById(nonExistingTaskId);

        assertTrue(task.isEmpty());
    }
}
