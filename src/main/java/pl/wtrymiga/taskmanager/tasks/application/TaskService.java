package pl.wtrymiga.taskmanager.tasks.application;

import java.time.Clock;
import java.time.Instant;
import java.util.function.Predicate;

import pl.wtrymiga.taskmanager.audit.domain.TaskChange;
import pl.wtrymiga.taskmanager.audit.domain.TaskChangeType;
import pl.wtrymiga.taskmanager.audit.domain.application.port.out.TaskChangeRepository;
import pl.wtrymiga.taskmanager.tasks.application.port.in.TaskCommandUseCase;
import pl.wtrymiga.taskmanager.tasks.application.port.out.TaskCodeGenerator;
import pl.wtrymiga.taskmanager.tasks.application.port.out.TaskHierarchyService;
import pl.wtrymiga.taskmanager.tasks.application.port.out.TaskRepository;
import pl.wtrymiga.taskmanager.tasks.domain.Task;
import pl.wtrymiga.taskmanager.tasks.domain.TaskId;
import pl.wtrymiga.taskmanager.tasks.domain.TaskStatus;
import pl.wtrymiga.taskmanager.tasks.domain.TaskVisibility;

public class TaskService implements TaskCommandUseCase {
	private final TaskRepository taskRepository;
	private final TaskHierarchyService hierarchyService;
	private final TaskCodeGenerator taskCodeGenerator;
	private final TaskChangeRepository changeRepository;
	private final Clock clock;

	public TaskService(TaskRepository taskRepository, TaskHierarchyService hierarchyService,
			TaskCodeGenerator taskCodeGenerator, Clock clock, TaskChangeRepository changeRepository) {
		this.taskRepository = taskRepository;
		this.hierarchyService = hierarchyService;
		this.taskCodeGenerator = taskCodeGenerator;
		this.clock = clock;
		this.changeRepository = changeRepository;
	}

	@Override
	public Task create(String title, String description, TaskVisibility visibility, TaskId parentId, String actorId) {
		Predicate<TaskId> parentExists = hierarchyService::parentExists;
		Predicate<TaskId> createsCycle = q -> hierarchyService.createsCycle(null, q);
		Task saved = taskRepository.save(Task.create(title, description, visibility, parentId, actorId, clock,
				taskCodeGenerator, parentExists, createsCycle));
		changeRepository.save(new TaskChange(saved.getId(), TaskChangeType.CREATED, actorId, Instant.now(clock), null));
		return saved;
	}

	@Override
	public Task patch(TaskId id, String title, String description, TaskId parentId, String actorId) {
		Task task = taskRepository.findById(id).orElseThrow();
		Instant now = Instant.now(clock);
		task.patch(title, description, parentId, actorId, now, hierarchyService::parentExists,
				q -> hierarchyService.createsCycle(id, q));
		Task saved = taskRepository.save(task);
		changeRepository.save(new TaskChange(id, TaskChangeType.UPDATED, actorId, now, null));
		return saved;
	}

	@Override
	public Task complete(TaskId id, String actorId) {
		Task task = taskRepository.findById(id).orElseThrow();
		Instant now = Instant.now(clock);
		task.complete(actorId, now, () -> taskRepository.areAllChildrenCompleted(id));
		Task saved = taskRepository.save(task);
		changeRepository.save(new TaskChange(id, TaskChangeType.COMPLETED, actorId, now, null));
		return saved;
	}

	@Override
	public void delete(TaskId id, String actorId) {
		Task task = taskRepository.findById(id).orElseThrow();
		if (task.getStatus() != TaskStatus.PENDING)
			throw new IllegalStateException("not pending");

		taskRepository.deleteRecursively(id);
		changeRepository.save(new TaskChange(id, TaskChangeType.DELETED, actorId, Instant.now(clock), null));
	}

	@Override
	public long expirePending() {
		Instant now = Instant.now(clock);
		var expired = taskRepository.streamPendingDueBefore(now).peek(q -> q.expireIfDue(now)).map(taskRepository::save)
				.filter(q -> q.getStatus() == TaskStatus.EXPIRED).toList();
		expired.forEach(
				q -> changeRepository.save(new TaskChange(q.getId(), TaskChangeType.EXPIRED, "system", now, null)));
		return expired.size();
	}

}
