package pl.wtrymiga.taskmanager.web.tasks;

import java.time.Instant;
import java.util.stream.Stream;

import org.springframework.format.annotation.DateTimeFormat;
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
public class TaskListController {
	private final TaskQueryUseCase taskQueryUseCase;

	TaskListController(TaskQueryUseCase taskQueryUseCase) {
		this.taskQueryUseCase = taskQueryUseCase;
	}

	@GetMapping
	Stream<TaskListItem> list(@RequestParam(required = false) TaskStatus status,
			@RequestParam(required = false) TaskVisibility visibility,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant after,
			@RequestParam(defaultValue = "20") int limit) {
		return taskQueryUseCase.list(status, visibility, after, limit)
				.map(q -> new TaskListItem(q.getId() == null ? null : q.getId().value(), q.getTitle(), q.getStatus(),
						q.getVisibility(), q.getCreatedAt(), q.getTaskCode()));
	}
}
