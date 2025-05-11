package pl.wtrymiga.taskmanager.tasks.domain;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Task {
	private TaskId id;
	private String title;
	private String description;
	private TaskStatus status;
	private TaskVisibility visibility;
	private Instant createdAt;
	private String createdBy;
	private Instant modifiedAt;
	private String modifiedBy;
	private LocalDate dueDate;
	private TaskId parentId;
	private String taskCode;
	private long version;

	public static Task create(String title, String description, TaskVisibility visibility, TaskId parentId,
			String actorId, Clock clock, TaskCodeGenerator taskCodeGenerator, Predicate<TaskId> parentExists,
			Predicate<TaskId> createsCycle) {
		Objects.requireNonNull(title);
		if (title.isBlank())
			throw new IllegalArgumentException("blank title");
		if (parentId != null) {
			if (!parentExists.test(parentId))
				throw new IllegalArgumentException("parent not found");
			if (createsCycle.test(parentId))
				throw new IllegalArgumentException("cycle detected");
		}
		Instant now = Instant.now(clock);
		LocalDate due = addBusinessDays(LocalDate.ofInstant(now, ZoneId.systemDefault()), 10);
		String code = taskCodeGenerator.nextCode(due.getYear());
		return new Task(null, title, description, TaskStatus.PENDING, visibility, now, actorId, null, null, due,
				parentId, code, 0);
	}

	public void patch(String newTitle, String newDescription, TaskId newParentId, String actorId, Instant now,
			Predicate<TaskId> parentExists, Predicate<TaskId> createsCycle) {
		if (status != TaskStatus.PENDING)
			throw new IllegalStateException("not editable");
		Optional.ofNullable(newTitle).filter(t -> !t.isBlank()).ifPresent(t -> title = t);
		Optional.ofNullable(newDescription).ifPresent(d -> description = d);
		if (newParentId != null && !Objects.equals(newParentId, id)) {
			if (!parentExists.test(newParentId))
				throw new IllegalArgumentException("parent not found");
			if (createsCycle.test(newParentId))
				throw new IllegalArgumentException("cycle detected");
			parentId = newParentId;
		}
		modifiedAt = now;
		modifiedBy = actorId;
		++version;
	}

	public void complete(String actorId, Instant now, Supplier<Boolean> allChildrenCompleted) {
		if (status != TaskStatus.PENDING)
			throw new IllegalStateException("not pending");
		String lower = title.toLowerCase();
		if (lower.contains("wip") || lower.contains("draft"))
			throw new IllegalStateException("forbidden title");
		if (!allChildrenCompleted.get())
			throw new IllegalStateException("children not completed");
		status = TaskStatus.COMPLETED;
		modifiedAt = now;
		modifiedBy = actorId;
		++version;
	}

	public void expireIfDue(Instant now) {
		if (status == TaskStatus.PENDING && now.isAfter(dueDate.atStartOfDay(ZoneId.systemDefault()).toInstant()))
			status = TaskStatus.EXPIRED;
	}

	private static LocalDate addBusinessDays(LocalDate startDate, int businessDays) {
		return IntStream.iterate(1, q -> ++q).mapToObj(q -> startDate.plusDays(q))
				.filter(d -> d.getDayOfWeek() != DayOfWeek.SATURDAY && d.getDayOfWeek() != DayOfWeek.SUNDAY)
				.limit(businessDays).reduce((p, r) -> r).orElse(startDate);
	}
}
