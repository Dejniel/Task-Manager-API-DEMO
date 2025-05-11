package pl.wtrymiga.taskmanager.web.tasks;

import java.time.Instant;

public record CursorPageRequest(Instant after, int limit) {
	public static CursorPageRequest of(Instant after, int limit) {
		return new CursorPageRequest(after, limit);
	}
}
