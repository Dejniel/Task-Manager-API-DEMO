package pl.wtrymiga.taskmanager.tasks.infra.jpa;

import java.time.Clock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.wtrymiga.taskmanager.audit.domain.application.port.out.TaskChangeRepository;
import pl.wtrymiga.taskmanager.tasks.application.TaskQueryService;
import pl.wtrymiga.taskmanager.tasks.application.TaskService;
import pl.wtrymiga.taskmanager.tasks.application.port.in.TaskCommandUseCase;
import pl.wtrymiga.taskmanager.tasks.application.port.in.TaskQueryUseCase;
import pl.wtrymiga.taskmanager.tasks.application.port.out.TaskCodeGenerator;
import pl.wtrymiga.taskmanager.tasks.application.port.out.TaskHierarchyService;
import pl.wtrymiga.taskmanager.tasks.application.port.out.TaskRepository;
import pl.wtrymiga.taskmanager.tasks.infra.codegen.TaskCodeGeneratorInMemorySequence;
import pl.wtrymiga.taskmanager.tasks.infra.sched.TaskExpirationJob;

@Configuration
class TasksInfrastructureConfiguration {
	@Bean
	Clock clock() {
		return Clock.systemDefaultZone();
	}

	@Bean
	TaskCodeGenerator taskCodeGenerator() {
		return new TaskCodeGeneratorInMemorySequence();
	}

	@Bean
	TaskRepository taskRepository(TaskJpaRepository repo) {
		return new TaskJpaAdapter(repo);
	}

	@Bean
	TaskHierarchyService taskHierarchyService(TaskJpaRepository repo) {
		return new TaskHierarchyAdapterJpa(repo);
	}

	@Bean
	TaskService taskService(TaskRepository repo, TaskHierarchyService hierarchy, TaskCodeGenerator generator,
			Clock clock, TaskChangeRepository repoChange) {
		return new TaskService(repo, hierarchy, generator, clock, repoChange);
	}

	@Bean
	TaskQueryUseCase taskQueryUseCase(TaskRepository repo) {
		return new TaskQueryService(repo);
	}

	@Bean
	org.springframework.scheduling.annotation.SchedulingConfigurer schedule(TaskCommandUseCase cmd) {
		return taskRegistrar -> {
		};
	}

	@Bean
	TaskExpirationJob expirationJob(TaskCommandUseCase cmd) {
		return new TaskExpirationJob(cmd);
	}

}
