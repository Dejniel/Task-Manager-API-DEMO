package pl.wtrymiga.taskmanager.audit.domain.application.port.out;

import java.util.List;

import pl.wtrymiga.taskmanager.audit.domain.TaskChange;
import pl.wtrymiga.taskmanager.tasks.domain.TaskId;

public interface TaskChangeRepository {
	TaskChange save(TaskChange change);

	List<TaskChange> findByTaskIdOrderByOccurredAtAsc(TaskId taskId);
}
