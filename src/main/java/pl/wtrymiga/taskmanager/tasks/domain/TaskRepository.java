package pl.wtrymiga.taskmanager.tasks.domain;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Stream;

public interface TaskRepository {
	Task save(Task task);

	Optional<Task> findById(TaskId id);

	boolean existsById(TaskId id);

	boolean areAllChildrenCompleted(TaskId parentId);

	void deleteRecursively(TaskId id);

	Stream<Task> streamPendingDueBefore(Instant instant);
}
