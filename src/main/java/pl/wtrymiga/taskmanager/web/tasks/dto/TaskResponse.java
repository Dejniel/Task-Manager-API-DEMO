package pl.wtrymiga.taskmanager.web.tasks.dto;

import java.time.Instant;
import java.time.LocalDate;

import pl.wtrymiga.taskmanager.tasks.domain.TaskStatus;
import pl.wtrymiga.taskmanager.tasks.domain.TaskVisibility;

public record TaskResponse(Long id, String title, String description, TaskStatus status, TaskVisibility visibility,
		Instant createdAt, String createdBy, Instant modifiedAt, String modifiedBy, LocalDate dueDate, Long parentId,
		String taskCode, long version) {
}