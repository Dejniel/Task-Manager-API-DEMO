package pl.wtrymiga.taskmanager.tasks.infra.jpa;

import java.time.Clock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.wtrymiga.taskmanager.tasks.application.TaskService;
import pl.wtrymiga.taskmanager.tasks.application.port.out.TaskCodeGenerator;
import pl.wtrymiga.taskmanager.tasks.application.port.out.TaskHierarchyService;
import pl.wtrymiga.taskmanager.tasks.application.port.out.TaskRepository;
import pl.wtrymiga.taskmanager.tasks.infra.codegen.TaskCodeGeneratorInMemorySequence;

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
			Clock clock) {
		return new TaskService(repo, hierarchy, generator, clock);
	}
}
