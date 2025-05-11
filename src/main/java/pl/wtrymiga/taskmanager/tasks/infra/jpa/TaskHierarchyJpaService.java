package pl.wtrymiga.taskmanager.tasks.infra.jpa;

import java.util.Optional;

import pl.wtrymiga.taskmanager.tasks.domain.TaskHierarchyService;
import pl.wtrymiga.taskmanager.tasks.domain.TaskId;

public class TaskHierarchyJpaService implements TaskHierarchyService {
	private final TaskJpaRepository repo;

	public TaskHierarchyJpaService(TaskJpaRepository repo) {
		this.repo = repo;
	}

	@Override
	public boolean parentExists(TaskId parentId) {
		return repo.existsById(parentId.value());
	}

	@Override
	public boolean createsCycle(TaskId taskId, TaskId newParentId) {
		if (taskId == null)
			return false;
		var p = Optional.of(newParentId).map(TaskId::value);
		while (p.isPresent()) {
			if (p.get().equals(taskId.value()))
				return true;
			p = repo.findById(p.get()).map(TaskEntity::getParentId);
		}
		return false;
	}
}