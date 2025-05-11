package pl.wtrymiga.taskmanager.tasks.http;

import pl.wtrymiga.taskmanager.tasks.domain.Task;
import pl.wtrymiga.taskmanager.tasks.http.dto.TaskResponse;

final class TaskMapper {
	static TaskResponse toResponse(Task task) {
		return new TaskResponse(task.getId() == null ? null : task.getId().value(), task.getTitle(),
				task.getDescription(), task.getStatus(), task.getVisibility(), task.getCreatedAt(), task.getCreatedBy(),
				task.getModifiedAt(), task.getModifiedBy(), task.getDueDate(),
				task.getParentId() == null ? null : task.getParentId().value(), task.getTaskCode());
	}
}
