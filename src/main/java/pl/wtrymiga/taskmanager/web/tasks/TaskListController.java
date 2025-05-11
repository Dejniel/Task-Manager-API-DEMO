package pl.wtrymiga.taskmanager.web.tasks;

import java.time.Instant;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.wtrymiga.taskmanager.tasks.application.port.in.TaskQueryUseCase;
import pl.wtrymiga.taskmanager.tasks.domain.TaskStatus;
import pl.wtrymiga.taskmanager.tasks.domain.TaskVisibility;
import pl.wtrymiga.taskmanager.web.tasks.dto.TaskListItem;

@RestController
@RequestMapping("/tasks")
class TaskListController {
	private final TaskQueryUseCase taskQueryUseCase;

	TaskListController(TaskQueryUseCase taskQueryUseCase) {
		this.taskQueryUseCase = taskQueryUseCase;
	}

	@Transactional(readOnly = true)
	@GetMapping
	CursorPage<TaskListItem> list(@RequestParam(name = "status", required = false) TaskStatus status,
			@RequestParam(name = "visibility", required = false) TaskVisibility visibility,
			@RequestParam(name = "after", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant after,
			@RequestParam(name = "limit", defaultValue = "20") int limit) {

		var items = taskQueryUseCase.list(status, visibility, after, limit)
				.map(t -> new TaskListItem(t.getId() == null ? null : t.getId().value(), t.getTitle(), t.getStatus(),
						t.getVisibility(), t.getCreatedAt(), t.getTaskCode()))
				.toList();
		Instant next = items.size() == limit ? items.getLast().createdAt() : null;
		return new CursorPage<>(items, next);
	}
}
