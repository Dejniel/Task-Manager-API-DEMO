package pl.wtrymiga.taskmanager.tasks.application.port.in;

import java.time.Instant;
import java.util.stream.Stream;

import pl.wtrymiga.taskmanager.tasks.domain.Task;
import pl.wtrymiga.taskmanager.tasks.domain.TaskStatus;
import pl.wtrymiga.taskmanager.tasks.domain.TaskVisibility;

public interface TaskQueryUseCase {
	Stream<Task> list(TaskStatus status, TaskVisibility visibility, Instant after, int limit);
}