package pl.wtrymiga.taskmanager.tasks.infra.jpa;

import java.time.Instant;
import java.time.LocalDate;
import java.util.stream.Stream;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import pl.wtrymiga.taskmanager.tasks.domain.TaskStatus;
import pl.wtrymiga.taskmanager.tasks.domain.TaskVisibility;

public interface TaskJpaRepository extends JpaRepository<TaskEntity, Long> {
	boolean existsByParentId(Long parentId);

	boolean existsByParentIdAndStatusNot(Long parentId, TaskStatus status);

	Stream<TaskEntity> streamByStatusAndDueDateBefore(TaskStatus status, LocalDate before);

	void deleteByParentId(Long parentId);

	@Query("""
			select t from TaskEntity t
			where (:status is null or t.status=:status)
			  and (:visibility is null or t.visibility=:visibility)
			  and (:after is null or t.createdAt<:after)
			order by t.createdAt desc
			""")
	Stream<TaskEntity> search(TaskStatus status, TaskVisibility visibility, Instant after, Pageable pageable);

}