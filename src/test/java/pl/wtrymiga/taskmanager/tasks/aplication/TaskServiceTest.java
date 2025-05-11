package pl.wtrymiga.taskmanager.tasks.aplication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;

import pl.wtrymiga.taskmanager.audit.domain.application.port.out.TaskChangeRepository;
import pl.wtrymiga.taskmanager.tasks.application.TaskService;
import pl.wtrymiga.taskmanager.tasks.application.port.out.TaskCodeGenerator;
import pl.wtrymiga.taskmanager.tasks.application.port.out.TaskHierarchyService;
import pl.wtrymiga.taskmanager.tasks.application.port.out.TaskRepository;
import pl.wtrymiga.taskmanager.tasks.domain.Task;
import pl.wtrymiga.taskmanager.tasks.domain.TaskId;
import pl.wtrymiga.taskmanager.tasks.domain.TaskStatus;
import pl.wtrymiga.taskmanager.tasks.domain.TaskVisibility;

public class TaskServiceTest {
	private final TaskRepository repo = mock(TaskRepository.class);
	private final TaskHierarchyService hierarchy = mock(TaskHierarchyService.class);
	private final TaskCodeGenerator gen = y -> "TASK-" + y + "-1";
	private final Clock clock = Clock.fixed(Instant.parse("2025-05-10T10:00:00Z"), ZoneOffset.UTC);
	private final TaskChangeRepository changes = mock(TaskChangeRepository.class);
	private final TaskService svc = new TaskService(repo, hierarchy, gen, clock, changes);

	@Test
	void createPersistsAndAudits() {
		when(hierarchy.parentExists(null)).thenReturn(true);
		Task unsaved = Task.create("t", "d", TaskVisibility.PUBLIC, null, "u", clock, gen, x -> true, x -> false);
		Task saved = new Task(TaskId.of(1), "t", "d", TaskStatus.PENDING, TaskVisibility.PUBLIC, unsaved.getCreatedAt(),
				"u", null, null, unsaved.getDueDate(), null, "TASK-2025-1", 0);
		when(repo.save(any())).thenReturn(saved);
		Task q = svc.create("t", "d", TaskVisibility.PUBLIC, null, "u");
		assertThat(q.getId().value()).isEqualTo(1);
		verify(changes).save(any());
	}
}
