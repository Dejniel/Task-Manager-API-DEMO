package pl.wtrymiga.taskmanager.tasks.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;

import pl.wtrymiga.taskmanager.tasks.application.port.out.TaskCodeGenerator;

public class TaskDomainTest {
	private final Clock clock = Clock.fixed(Instant.parse("2025-05-10T10:00:00Z"), ZoneOffset.UTC);
	private final TaskCodeGenerator generator = y -> "TASK-" + y + "-1";

	@Test
	void createSetsDefaults() {
		Task q = Task.create("t", "d", TaskVisibility.PUBLIC, null, "u", clock, generator, x -> true, x -> false);
		assertThat(q.getStatus()).isEqualTo(TaskStatus.PENDING);
		assertThat(q.getDueDate()).isEqualTo(LocalDate.of(2025, 5, 23)); // 10 dni roboczych
		assertThat(q.getTaskCode()).isEqualTo("TASK-2025-1");
	}

	@Test
	void completeRejectsWip() {
		Task q = Task.create("wip", "d", TaskVisibility.PUBLIC, null, "u", clock, generator, x -> true, x -> false);
		assertThatThrownBy(() -> q.complete("u", Instant.now(clock), () -> true))
				.isInstanceOf(IllegalStateException.class);
	}

	@Test
	void expiresAfterDue() {
		Task q = Task.create("t", "d", TaskVisibility.PUBLIC, null, "u", clock, generator, x -> true, x -> false);
		q.expireIfDue(Instant.parse("2025-05-25T00:00:00Z"));
		assertThat(q.getStatus()).isEqualTo(TaskStatus.EXPIRED);
	}
}
