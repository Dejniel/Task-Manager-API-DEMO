package pl.wtrymiga.taskmanager.audit.infra.jpa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.wtrymiga.taskmanager.audit.domain.application.port.out.TaskChangeRepository;

@Configuration
public class AuditInfrastructureConfiguration {
	@Bean
	TaskChangeRepository taskChangeRepository(TaskChangeJpaRepository repo) {
		return new TaskChangeJpaAdapter(repo);
	}
}
