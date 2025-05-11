package pl.wtrymiga.taskmanager.tasks.application;

import java.time.Instant;
import java.util.stream.Stream;

import pl.wtrymiga.taskmanager.tasks.application.port.in.TaskQueryUseCase;
import pl.wtrymiga.taskmanager.tasks.application.port.out.TaskRepository;
import pl.wtrymiga.taskmanager.tasks.domain.Task;
import pl.wtrymiga.taskmanager.tasks.domain.TaskStatus;
import pl.wtrymiga.taskmanager.tasks.domain.TaskVisibility;

public class TaskQueryService implements TaskQueryUseCase {
	private final TaskRepository taskRepository;

	public TaskQueryService(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	@Override
	public Stream<Task> list(TaskStatus status, TaskVisibility visibility, Instant after, int limit) {
		return taskRepository.streamFiltered(status, visibility, after, limit);
	}
}