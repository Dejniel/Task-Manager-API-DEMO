package pl.wtrymiga.taskmanager.tasks.http.dto;

public record TaskPatchRequest(String title, String description, Long parentId) {
}