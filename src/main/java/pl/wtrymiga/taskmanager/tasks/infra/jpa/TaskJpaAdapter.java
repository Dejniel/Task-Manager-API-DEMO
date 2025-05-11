package pl.wtrymiga.taskmanager.tasks.infra.jpa;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;
import java.util.stream.Stream;

import pl.wtrymiga.taskmanager.tasks.application.port.out.TaskRepository;
import pl.wtrymiga.taskmanager.tasks.domain.Task;
import pl.wtrymiga.taskmanager.tasks.domain.TaskId;
import pl.wtrymiga.taskmanager.tasks.domain.TaskStatus;
import pl.wtrymiga.taskmanager.tasks.domain.TaskVisibility;

public class TaskJpaAdapter implements TaskRepository {
	private final TaskJpaRepository repo;

	public TaskJpaAdapter(TaskJpaRepository repo) {
		this.repo = repo;
	}

	@Override
	public Task save(Task task) {
		return toDomain(repo.save(toEntity(task)));
	}

	@Override
	public Optional<Task> findById(TaskId id) {
		return repo.findById(id.value()).map(this::toDomain);
	}

	@Override
	public boolean existsById(TaskId id) {
		return repo.existsById(id.value());
	}

	@Override
	public boolean areAllChildrenCompleted(TaskId parentId) {
		return !repo.existsByParentIdAndStatusNot(parentId.value(), TaskStatus.COMPLETED);
	}

	@Override
	public void deleteRecursively(TaskId id) {
		repo.deleteById(id.value());
		repo.deleteByParentId(id.value());
	}

	@Override
	public Stream<Task> streamPendingDueBefore(Instant instant) {
		var date = instant.atZone(ZoneId.systemDefault()).toLocalDate();
		return repo.streamByStatusAndDueDateBefore(TaskStatus.PENDING, date).map(this::toDomain);
	}

	@Override
	public Stream<Task> streamFiltered(TaskStatus status, TaskVisibility visibility, Instant after, int limit) {
		return repo.search(status, visibility, after, org.springframework.data.domain.PageRequest.of(0, limit))
				.map(this::toDomain);
	}

	private Task toDomain(TaskEntity e) {
		return new Task(e.getId() == null ? null : TaskId.of(e.getId()), e.getTitle(), e.getDescription(),
				e.getStatus(), e.getVisibility(), e.getCreatedAt(), e.getCreatedBy(), e.getModifiedAt(),
				e.getModifiedBy(), e.getDueDate(), e.getParentId() == null ? null : TaskId.of(e.getParentId()),
				e.getTaskCode(), e.getVersion());
	}

	private TaskEntity toEntity(Task d) {
		return new TaskEntity(d.getId() == null ? null : d.getId().value(), d.getTitle(), d.getDescription(),
				d.getStatus(), d.getVisibility(), d.getCreatedAt(), d.getCreatedBy(), d.getModifiedAt(),
				d.getModifiedBy(), d.getDueDate(), d.getParentId() == null ? null : d.getParentId().value(),
				d.getTaskCode(), d.getVersion());
	}

}