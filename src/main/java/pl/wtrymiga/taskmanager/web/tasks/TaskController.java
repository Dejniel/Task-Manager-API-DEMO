package pl.wtrymiga.taskmanager.web.tasks;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.wtrymiga.taskmanager.tasks.application.TaskService;
import pl.wtrymiga.taskmanager.tasks.domain.TaskId;
import pl.wtrymiga.taskmanager.web.tasks.dto.TaskCreateRequest;
import pl.wtrymiga.taskmanager.web.tasks.dto.TaskPatchRequest;
import pl.wtrymiga.taskmanager.web.tasks.dto.TaskResponse;
import pl.wtrymiga.taskmanager.web.tasks.mapper.TaskMapper;

@RestController
@RequestMapping("/tasks")
class TaskController {
	private final TaskService taskService;

	TaskController(TaskService taskService) {
		this.taskService = taskService;
	}

	@PostMapping
	TaskResponse create(@RequestBody TaskCreateRequest request, @RequestHeader("X-User-Id") String actorId) {
		return TaskMapper.toResponse(taskService.create(request.title(), request.description(), request.visibility(),
				request.parentId() == null ? null : TaskId.of(request.parentId()), actorId));
	}

	@PatchMapping("/{id}")
	TaskResponse patch(@PathVariable long id, @RequestBody TaskPatchRequest request,
			@RequestHeader("X-User-Id") String actorId) {
		return TaskMapper.toResponse(taskService.patch(TaskId.of(id), request.title(), request.description(),
				request.parentId() == null ? null : TaskId.of(request.parentId()), actorId));
	}

	@PutMapping("/{id}/complete")
	TaskResponse complete(@PathVariable long id, @RequestHeader("X-User-Id") String actorId) {
		return TaskMapper.toResponse(taskService.complete(TaskId.of(id), actorId));
	}

	@DeleteMapping("/{id}")
	void delete(@PathVariable long id, @RequestHeader("X-User-Id") String actorId) {
		taskService.delete(TaskId.of(id), actorId);
	}
}
