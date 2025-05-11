package pl.wtrymiga.taskmanager.web.audit;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.wtrymiga.taskmanager.audit.domain.TaskChange;
import pl.wtrymiga.taskmanager.audit.domain.application.port.out.TaskChangeRepository;
import pl.wtrymiga.taskmanager.tasks.domain.TaskId;

@RestController
@RequestMapping("/tasks/{taskId}/changes")
class TaskChangeController {
	private final TaskChangeRepository repo;

	TaskChangeController(TaskChangeRepository repo) {
		this.repo = repo;
	}

	@GetMapping
	List<TaskChange> list(@PathVariable("taskId") long taskId) {
		return repo.findByTaskIdOrderByOccurredAtAsc(TaskId.of(taskId));
	}
}
