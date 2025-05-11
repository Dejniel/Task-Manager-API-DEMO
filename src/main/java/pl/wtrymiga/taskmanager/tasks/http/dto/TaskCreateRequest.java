package pl.wtrymiga.taskmanager.tasks.http.dto;

import pl.wtrymiga.taskmanager.tasks.domain.TaskVisibility;

public record TaskCreateRequest(String title, String description, TaskVisibility visibility, Long parentId) {
}