package pl.wtrymiga.taskmanager.tasks.application.port.out;

import pl.wtrymiga.taskmanager.tasks.domain.TaskId;

public interface TaskHierarchyService {
	boolean parentExists(TaskId parentId);

	boolean createsCycle(TaskId taskId, TaskId newParentId);
}