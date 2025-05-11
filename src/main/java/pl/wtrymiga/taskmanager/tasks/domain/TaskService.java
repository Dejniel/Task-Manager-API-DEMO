package pl.wtrymiga.taskmanager.tasks.domain;

import java.time.Clock;
import java.time.Instant;
import java.util.function.Predicate;

public class TaskService {
	private final TaskRepository taskRepository;
	private final TaskHierarchyService hierarchyService;
	private final TaskCodeGenerator taskCodeGenerator;
	private final Clock clock;

	public TaskService(TaskRepository taskRepository, TaskHierarchyService hierarchyService,
			TaskCodeGenerator taskCodeGenerator, Clock clock) {
		this.taskRepository = taskRepository;
		this.hierarchyService = hierarchyService;
		this.taskCodeGenerator = taskCodeGenerator;
		this.clock = clock;
	}

	public Task create(String title, String description, TaskVisibility visibility, TaskId parentId, String actorId) {
		Predicate<TaskId> parentExists = hierarchyService::parentExists;
		Predicate<TaskId> createsCycle = q -> hierarchyService.createsCycle(null, q);
		return taskRepository.save(Task.create(title, description, visibility, parentId, actorId, clock,
				taskCodeGenerator, parentExists, createsCycle));
	}

	public Task patch(TaskId id, String title, String description, TaskId parentId, String actorId) {
		Task task = taskRepository.findById(id).orElseThrow();
		Instant now = Instant.now(clock);
		task.patch(title, description, parentId, actorId, now, hierarchyService::parentExists,
				q -> hierarchyService.createsCycle(id, q));
		return taskRepository.save(task);
	}

	public Task complete(TaskId id, String actorId) {
		Task task = taskRepository.findById(id).orElseThrow();
		Instant now = Instant.now(clock);
		task.complete(actorId, now, () -> taskRepository.areAllChildrenCompleted(id));
		return taskRepository.save(task);
	}

	public void delete(TaskId id, String actorId) {
		taskRepository.deleteRecursively(id);
	}

	public long expirePending() {
		Instant now = Instant.now(clock);
		return taskRepository.streamPendingDueBefore(now).peek(q -> q.expireIfDue(now)).map(taskRepository::save)
				.count();
	}
}
