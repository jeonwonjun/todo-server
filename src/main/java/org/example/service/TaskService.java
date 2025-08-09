package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.constants.TaskStatus;
import org.example.model.Task;
import org.example.persist.entity.TaskEntity;
import org.example.persist.TaskRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j // log사용
@Service // service 클래스 이기 때문에
@RequiredArgsConstructor // 개발을 편하게 하기 위해
public class TaskService {

    private final TaskRepository taskRepository;

    public Task add(String title, String description, LocalDate dueDate) {
        var e = TaskEntity.builder()
                .title(title)
                .description(description)
                .dueDate(Date.valueOf(dueDate))
                .status(TaskStatus.TODO)
                .build();

        var saved = this.taskRepository.save(e);
        return entityToObject(saved);
    }

    // Task의 모든 목록을 조회하는 method
    public List<Task> getAll() {
        return this.taskRepository.findAll().stream()
                .map(this::entityToObject)
                .collect(Collectors.toList());
    }

    // dueDate에 해당하는 Task 목록 조회하는 method
    public List<Task> getByDueDate(String dueDate) {
        return this.taskRepository.findAllByDueDate(Date.valueOf(dueDate)).stream()
                .map(this::entityToObject)
                .collect(Collectors.toList());
    }

    // status에 해당하는 Task 목록 조회하는 method
    public List<Task> getByStatus(TaskStatus status) {
        return this.taskRepository.findAllByStatus((status)).stream()
                .map(this::entityToObject)
                .collect(Collectors.toList());
    }

    // 특정 ID에 해당하는 Task 데이터 조회하는 method
    public Task getOne(Long id) {
        var entity = this.getById(id);
        return this.entityToObject(entity);
    }

    private TaskEntity getById(Long id) {
        return this.taskRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(String.format("not exists task id {%d}", id)));
    }



    private Task entityToObject(TaskEntity e) {
        return Task.builder()
                .id(e.getId())
                .title(e.getTitle())
                .description(e.getDescription())
                .status(e.getStatus())
                .dueDate(e.getDueDate().toString())
                .createdAt(e.getCreatedAt().toLocalDateTime())
                .updatedAt(e.getUpdatedAt().toLocalDateTime())
                .build();
    }
}
