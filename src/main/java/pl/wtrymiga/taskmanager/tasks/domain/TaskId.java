package pl.wtrymiga.taskmanager.tasks.domain;

public record TaskId(long value) {
	public static TaskId of(long value) {
		return new TaskId(value);
	}
}