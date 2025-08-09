package org.example.service;

import org.example.constants.TaskStatus;
import org.example.persist.TaskRepository;
import org.example.persist.entity.TaskEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.Invocation;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    // Mock 객체 생성 가능
    // 해당 객체를 이용하여 실제 객체를 대체하고 Mock객체의 동작을 검증 가능
    // 해당 클래스와 라이브러리의 의존성을 완전히 제거하여 독립적인 테스트 가능
    @Mock
    private TaskRepository taskRepository;

    // 해당 클래스에 인스턴스를 생성하면서 Mock객체를 포함해서 모든 의존성을 taskService에 자동으로 주입
    @InjectMocks
    private TaskService taskService;

    @Test
    @DisplayName("할 일 추가 기능 테스트") // 해당 테스트 주석
    void add() {
        var title = "test";
        var description = "test description";
        var dueDate = LocalDate.now();

        // 실제 데이터베이스와 연결하지 않기 때문에 우리가 새로 정의해야함
        when(taskRepository.save(any(TaskEntity.class)))
                .thenAnswer(Invocation -> {
                    var e = (TaskEntity) Invocation.getArgument(0);
                    e.setId(1L);
                    e.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                    e.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                    return e;
                });

        var actual = taskService.add(title, description, dueDate);

        verify(taskRepository, times(1)).save(any());
        assertEquals(1L, actual.getId());
        assertEquals(title, actual.getTitle());
        assertEquals(dueDate.toString(), actual.getDueDate());
        assertEquals(TaskStatus.TODO, actual.getStatus());
        assertNotNull(actual.getCreatedAt());
        assertNotNull(actual.getUpdatedAt());
    }
}