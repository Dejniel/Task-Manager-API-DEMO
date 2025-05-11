package pl.wtrymiga.taskmanager.web.tasks.dto;

import java.time.Instant;

import pl.wtrymiga.taskmanager.tasks.domain.TaskStatus;
import pl.wtrymiga.taskmanager.tasks.domain.TaskVisibility;

public record TaskListItem(Long id, String title, TaskStatus status, TaskVisibility visibility, Instant createdAt,
		String taskCode) {
}