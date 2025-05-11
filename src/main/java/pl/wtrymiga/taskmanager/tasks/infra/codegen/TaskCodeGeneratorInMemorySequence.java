package pl.wtrymiga.taskmanager.tasks.infra.codegen;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import pl.wtrymiga.taskmanager.tasks.application.port.out.TaskCodeGenerator;

public class TaskCodeGeneratorInMemorySequence implements TaskCodeGenerator {
	private final ConcurrentHashMap<Integer, AtomicLong> sequences = new ConcurrentHashMap<>();

	@Override
	public String nextCode(int year) {
		return "TASK-" + year + "-" + sequences.computeIfAbsent(year, q -> new AtomicLong()).incrementAndGet();
	}
}