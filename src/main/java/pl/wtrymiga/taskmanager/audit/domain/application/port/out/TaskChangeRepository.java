package pl.wtrymiga.taskmanager.audit.domain.application.port.out;

import java.util.stream.Stream;

import pl.wtrymiga.taskmanager.audit.domain.TaskChange;
import pl.wtrymiga.taskmanager.tasks.domain.TaskId;

public interface TaskChangeRepository {
	TaskChange save(TaskChange change);

	Stream<TaskChange> findByTaskIdOrderByOccurredAtAsc(TaskId taskId);
}
