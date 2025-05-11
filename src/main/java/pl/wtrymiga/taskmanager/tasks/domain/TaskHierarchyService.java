package pl.wtrymiga.taskmanager.tasks.domain;

public interface TaskHierarchyService {
	boolean parentExists(TaskId parentId);

	boolean createsCycle(TaskId taskId, TaskId newParentId);
}