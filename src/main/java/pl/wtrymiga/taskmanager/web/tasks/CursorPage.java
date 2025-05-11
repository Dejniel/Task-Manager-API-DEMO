package pl.wtrymiga.taskmanager.web.tasks;

import java.time.Instant;
import java.util.List;

public record CursorPage<T>(List<T> items, Instant nextCursor) {
}
