package pl.wtrymiga.taskmanager.audit.domain;

import java.time.Instant;

import pl.wtrymiga.taskmanager.tasks.domain.TaskId;

public record TaskChange(TaskId taskId, TaskChangeType type, String actorId, Instant occurredAt, String details) {
}