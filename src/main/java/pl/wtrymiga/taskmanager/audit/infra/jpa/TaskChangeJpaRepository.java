package pl.wtrymiga.taskmanager.audit.infra.jpa;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskChangeJpaRepository extends JpaRepository<TaskChangeEntity, Long> {
	Stream<TaskChangeEntity> findByTaskIdOrderByOccurredAtAsc(Long taskId);
}