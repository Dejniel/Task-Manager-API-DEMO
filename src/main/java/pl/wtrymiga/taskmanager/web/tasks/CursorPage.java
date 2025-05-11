package pl.wtrymiga.taskmanager.web.tasks;

import java.util.List;

public record CursorPage<T>(List<T> items, String nextCursor) {
}
