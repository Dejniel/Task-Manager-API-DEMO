package pl.wtrymiga.taskmanager.tasks.infra.jpa;

import java.util.Optional;

import pl.wtrymiga.taskmanager.tasks.application.port.out.TaskHierarchyService;
import pl.wtrymiga.taskmanager.tasks.domain.TaskId;

public class TaskHierarchyAdapterJpa implements TaskHierarchyService {
	private final TaskJpaRepository repo;

	public TaskHierarchyAdapterJpa(TaskJpaRepository repo) {
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