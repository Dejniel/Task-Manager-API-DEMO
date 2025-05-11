package pl.wtrymiga.taskmanager.tasks.infra.jpa;

import java.time.Clock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.wtrymiga.taskmanager.tasks.domain.TaskCodeGenerator;
import pl.wtrymiga.taskmanager.tasks.domain.TaskCodeGeneratorInMemorySequence;
import pl.wtrymiga.taskmanager.tasks.domain.TaskHierarchyService;
import pl.wtrymiga.taskmanager.tasks.domain.TaskRepository;
import pl.wtrymiga.taskmanager.tasks.domain.TaskService;

@Configuration
class TasksJpaConfiguration {
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
		return new TaskHierarchyJpaService(repo);
	}

	@Bean
	TaskService taskService(TaskRepository repo, TaskHierarchyService hierarchy, TaskCodeGenerator generator,
			Clock clock) {
		return new TaskService(repo, hierarchy, generator, clock);
	}
}
