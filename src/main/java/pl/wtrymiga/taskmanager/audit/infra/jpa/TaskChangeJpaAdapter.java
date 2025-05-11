package pl.wtrymiga.taskmanager.audit.infra.jpa;

import java.time.Instant;
import java.util.stream.Stream;

import pl.wtrymiga.taskmanager.audit.domain.TaskChange;
import pl.wtrymiga.taskmanager.audit.domain.application.port.out.TaskChangeRepository;
import pl.wtrymiga.taskmanager.tasks.domain.TaskId;

public class TaskChangeJpaAdapter implements TaskChangeRepository {
	private final TaskChangeJpaRepository repo;

	public TaskChangeJpaAdapter(TaskChangeJpaRepository repo) {
		this.repo = repo;
	}

	@Override
	public TaskChange save(TaskChange c) {
		return toDomain(repo.save(toEntity(c)));
	}

	@Override
	public Stream<TaskChange> findByTaskIdOrderByOccurredAtAsc(TaskId taskId) {
		return repo.findByTaskIdOrderByOccurredAtAsc(taskId.value()).map(this::toDomain);
	}

	private TaskChange toDomain(TaskChangeEntity e) {
		return new TaskChange(TaskId.of(e.getTaskId()), e.getType(), e.getActorId(), e.getOccurredAt(), e.getDetails());
	}

	private TaskChangeEntity toEntity(TaskChange d) {
		return new TaskChangeEntity(null, d.taskId().value(), d.type(), d.actorId(),
				d.occurredAt() == null ? Instant.now() : d.occurredAt(), d.details());
	}
}