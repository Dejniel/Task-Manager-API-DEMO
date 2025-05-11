package pl.wtrymiga.taskmanager.web.tasks;

import java.io.PrintWriter;
import java.time.Instant;
import java.util.stream.Stream;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import pl.wtrymiga.taskmanager.tasks.application.port.in.TaskQueryUseCase;
import pl.wtrymiga.taskmanager.tasks.domain.Task;
import pl.wtrymiga.taskmanager.tasks.domain.TaskStatus;
import pl.wtrymiga.taskmanager.tasks.domain.TaskVisibility;

@RestController
@RequestMapping("/tasks/export")
public class TaskExportController {
	private final TaskQueryUseCase query;

	TaskExportController(TaskQueryUseCase query) {
		this.query = query;
	}

	@GetMapping(produces = "text/csv")
	void csv(HttpServletResponse resp, @RequestParam(required = false) TaskStatus status,
			@RequestParam(required = false) TaskVisibility visibility,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant after,
			@RequestParam(defaultValue = "1000") int limit) throws Exception {
		resp.setContentType("text/csv");
		try (PrintWriter w = resp.getWriter()) {
			w.println("id,title,status,visibility,createdAt,taskCode");
			query.list(status, visibility, after, limit)
					.forEach(t -> w.printf("%d,\"%s\",%s,%s,%s,%s%n", t.getId().value(),
							t.getTitle().replace("\"", "\"\""), t.getStatus(), t.getVisibility(), t.getCreatedAt(),
							t.getTaskCode()));
		}
	}

	@GetMapping(produces = MediaType.APPLICATION_NDJSON_VALUE)
	Stream<Task> jsonl(@RequestParam(required = false) TaskStatus status,
			@RequestParam(required = false) TaskVisibility visibility,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant after,
			@RequestParam(defaultValue = "1000") int limit) {
		return query.list(status, visibility, after, limit);
	}
}
