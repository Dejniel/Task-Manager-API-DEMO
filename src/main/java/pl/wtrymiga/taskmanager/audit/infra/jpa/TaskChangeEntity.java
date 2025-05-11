package pl.wtrymiga.taskmanager.audit.infra.jpa;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.wtrymiga.taskmanager.audit.domain.TaskChangeType;

@Entity
@Table(name = "task_changes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskChangeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long taskId;
	@Enumerated(EnumType.STRING)
	private TaskChangeType type;
	private String actorId;
	private Instant occurredAt;
	@Column(length = 2048)
	private String details;
}
