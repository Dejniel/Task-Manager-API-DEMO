package pl.wtrymiga.taskmanager.tasks.application.port.in;

import pl.wtrymiga.taskmanager.tasks.domain.Task;
import pl.wtrymiga.taskmanager.tasks.domain.TaskId;
import pl.wtrymiga.taskmanager.tasks.domain.TaskVisibility;

public interface TaskCommandUseCase {
	Task create(String title, String description, TaskVisibility visibility, TaskId parentId, String actorId);

	Task patch(TaskId id, String title, String description, TaskId parentId, String actorId);

	Task complete(TaskId id, String actorId);

	void delete(TaskId id, String actorId);

	long expirePending();
}