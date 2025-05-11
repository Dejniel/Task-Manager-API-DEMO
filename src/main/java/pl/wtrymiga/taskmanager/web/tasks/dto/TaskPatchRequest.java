package pl.wtrymiga.taskmanager.web.tasks.dto;

public record TaskPatchRequest(String title, String description, Long parentId) {
}