package com.emobile.springtodo.repository;

import com.emobile.springtodo.model.Task;
import com.emobile.springtodo.model.enums.TaskPriority;
import com.emobile.springtodo.model.enums.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {

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

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Page<Task> findAllByPage(Pageable pageable) {
        Integer total = jdbcTemplate.queryForObject(ROW_COUNT_SQL, new MapSqlParameterSource(), Integer.class);
        if (total == null) {
            total = 0;
        }

        List<Task> tasks = jdbcTemplate.query(FIND_ALL_BY_PAGE,
                Map.of(
                        "limit", pageable.getPageSize(),
                        "offset", pageable.getOffset()
                ),
                (rs, rowNum) -> Task.builder()
                        .id(rs.getLong("id"))
                        .title(rs.getString("title"))
                        .description(rs.getString("description"))
                        .status(TaskStatus.valueOf(rs.getString("status")))
                        .priority(TaskPriority.valueOf(rs.getString("priority")))
                        .createdAt(rs.getTimestamp("created_at").toInstant())
                        .lastModifiedAt(rs.getTimestamp("last_modified_at").toInstant())
                        .build()
        );

        return new PageImpl<>(tasks, pageable, total);
    }

    @Override
    public Optional<Task> findById(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_TASK_BY_ID,
                    Map.of("id", id),
                    (rs, rowNum) -> Task.builder()
                            .id(rs.getLong("id"))
                            .title(rs.getString("title"))
                            .description(rs.getString("description"))
                            .status(TaskStatus.valueOf(rs.getString("status")))
                            .priority(TaskPriority.valueOf(rs.getString("priority")))
                            .createdAt(rs.getTimestamp("created_at").toInstant())
                            .lastModifiedAt(rs.getTimestamp("last_modified_at").toInstant())
                            .build()
            ));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public Task save(Task task) {
        return jdbcTemplate.queryForObject(SAVE_TASK, Map.of(
                "title", task.getTitle(),
                "description", task.getDescription(),
                "status", task.getStatus(),
                "priority", task.getPriority()
        ), (rs, rowNum) -> Task.builder()
                .id(rs.getLong("id"))
                .title(rs.getString("title"))
                .description(rs.getString("description"))
                .status(TaskStatus.valueOf(rs.getString("status")))
                .priority(TaskPriority.valueOf(rs.getString("priority")))
                .createdAt(rs.getTimestamp("created_at").toInstant())
                .lastModifiedAt(rs.getTimestamp("last_modified_at").toInstant())
                .build());
    }

    @Override
    public Optional<Task> update(Task task) {
        int rowsAffected = jdbcTemplate.update(UPDATE_TASK, Map.of(
                "title", task.getTitle(),
                "description", task.getDescription(),
                "status", task.getStatus(),
                "priority", task.getPriority(),
                "id", task.getId()
        ));

        if (rowsAffected == 0) {
            return Optional.empty();
        }
        return findById(task.getId());
    }

    @Override
    public boolean delete(Task task) {
        return jdbcTemplate.update(DELETE_TASK, Map.of("id", task.getId())) > 0;
    }

    @Override
    public Optional<Task> updateTaskPriority(Task task) {
        int rowsAffected = jdbcTemplate.update(UPDATE_TASK_PRIORITY, Map.of("id", task.getId(), "priority", task.getPriority()));

        if (rowsAffected == 0) {
            return Optional.empty();
        }
        return findById(task.getId());
    }

    @Override
    public Optional<Task> updateTaskStatus(Task task) {
        int rowsAffected = jdbcTemplate.update(UPDATE_TASK_STATUS, Map.of("id", task.getId(), "status", task.getStatus()));

        if (rowsAffected == 0) {
            return Optional.empty();
        }
        return findById(task.getId());
    }
}
