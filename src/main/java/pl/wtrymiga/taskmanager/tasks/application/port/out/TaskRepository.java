package pl.wtrymiga.taskmanager.tasks.application.port.out;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Stream;

import pl.wtrymiga.taskmanager.tasks.domain.Task;
import pl.wtrymiga.taskmanager.tasks.domain.TaskId;

public interface TaskRepository {
	Task save(Task task);

	Optional<Task> findById(TaskId id);

	boolean existsById(TaskId id);

	boolean areAllChildrenCompleted(TaskId parentId);

	void deleteRecursively(TaskId id);

	Stream<Task> streamPendingDueBefore(Instant instant);
}
