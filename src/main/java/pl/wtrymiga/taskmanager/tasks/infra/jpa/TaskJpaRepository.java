package pl.wtrymiga.taskmanager.tasks.infra.jpa;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.wtrymiga.taskmanager.tasks.domain.TaskStatus;

public interface TaskJpaRepository extends JpaRepository<TaskEntity, Long> {
	boolean existsByParentId(Long parentId);
	boolean existsByParentIdAndStatusNot(Long parentId, TaskStatus status);
	Stream<TaskEntity> streamByStatusAndDueDateBefore(TaskStatus status, LocalDate before);
	void deleteByParentId(Long parentId);
}