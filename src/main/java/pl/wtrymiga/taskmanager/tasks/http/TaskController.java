package pl.wtrymiga.taskmanager.tasks.http;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.wtrymiga.taskmanager.tasks.domain.TaskService;
import pl.wtrymiga.taskmanager.tasks.http.dto.TaskCreateRequest;
import pl.wtrymiga.taskmanager.tasks.http.dto.TaskPatchRequest;
import pl.wtrymiga.taskmanager.tasks.http.dto.TaskResponse;

@RestController
@RequestMapping("/tasks")
class TaskController {
	private final TaskService taskService;

	TaskController(TaskService taskService) {
		this.taskService = taskService;
	}

	@PostMapping
	TaskResponse create(@RequestBody TaskCreateRequest request, @RequestHeader("X-User-Id") String actorId) {
		return null;
	}

	@PatchMapping("/{id}")
	TaskResponse patch(@PathVariable long id, @RequestBody TaskPatchRequest request,
			@RequestHeader("X-User-Id") String actorId) {
		return null;
	}

	@PutMapping("/{id}/complete")
	TaskResponse complete(@PathVariable long id, @RequestHeader("X-User-Id") String actorId) {
		return null;
	}

	@DeleteMapping("/{id}")
	void delete(@PathVariable long id, @RequestHeader("X-User-Id") String actorId) {
	}
}
