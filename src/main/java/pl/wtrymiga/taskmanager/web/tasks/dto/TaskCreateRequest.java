package pl.wtrymiga.taskmanager.web.tasks.dto;

import pl.wtrymiga.taskmanager.tasks.domain.TaskVisibility;

public record TaskCreateRequest(String title, String description, TaskVisibility visibility, Long parentId) {
}