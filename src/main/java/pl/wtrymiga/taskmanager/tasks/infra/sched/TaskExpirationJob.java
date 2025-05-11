package pl.wtrymiga.taskmanager.tasks.infra.sched;

import org.springframework.scheduling.annotation.Scheduled;

import pl.wtrymiga.taskmanager.tasks.application.port.in.TaskCommandUseCase;

public class TaskExpirationJob {
	private final TaskCommandUseCase taskCommandUseCase;

	public TaskExpirationJob(TaskCommandUseCase taskCommandUseCase) {
		this.taskCommandUseCase = taskCommandUseCase;
	}

	@Scheduled(cron = "0 0 * * * *")
	public void expire() {
		taskCommandUseCase.expirePending();
	}
}
