package pl.wtrymiga.taskmanager.web.tasks;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;
import pl.wtrymiga.taskmanager.tasks.application.port.in.TaskQueryUseCase;
import pl.wtrymiga.taskmanager.tasks.domain.TaskStatus;
import pl.wtrymiga.taskmanager.tasks.domain.TaskVisibility;

@RestController
@RequestMapping("/tasks/export")
public class TaskExportController {
	private final TaskQueryUseCase query;
	private final ObjectMapper mapper;

	TaskExportController(TaskQueryUseCase query, ObjectMapper mapper) {
		this.query = query;
		this.mapper = mapper;
	}

	@Transactional(readOnly = true)
	@GetMapping
	void export(HttpServletResponse resp, @RequestParam(name = "status", required = false) TaskStatus status,
			@RequestParam(name = "visibility", required = false) TaskVisibility visibility,
			@RequestParam(name = "after", required = false) @DateTimeFormat(iso = ISO.DATE_TIME) Instant after,
			@RequestParam(name = "limit", defaultValue = "1000") int limit,
			@RequestParam(name = "format", defaultValue = "jsonl") String format) throws IOException {

		if ("csv".equalsIgnoreCase(format)) {
			resp.setContentType("text/csv");
			try (PrintWriter w = resp.getWriter()) {
				w.println("id,title,status,visibility,createdAt,taskCode");
				query.list(status, visibility, after, limit)
						.forEach(t -> w.printf("%d,\"%s\",%s,%s,%s,%s%n", t.getId().value(),
								t.getTitle().replace("\"", "\"\""), t.getStatus(), t.getVisibility(), t.getCreatedAt(),
								t.getTaskCode()));
			}
		} else {
			resp.setContentType(MediaType.APPLICATION_NDJSON_VALUE);
			try (PrintWriter w = resp.getWriter()) {
				query.list(status, visibility, after, limit).forEach(t -> {
					try {
						w.println(mapper.writeValueAsString(t));
					} catch (JsonProcessingException e) {
						throw new IllegalStateException(e);
					}
				});
			}
		}
	}
}
