package pl.wtrymiga.taskmanager.audit.infra.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskChangeJpaRepository extends JpaRepository<TaskChangeEntity, Long> {
	List<TaskChangeEntity> findByTaskIdOrderByOccurredAtAsc(Long taskId);
}