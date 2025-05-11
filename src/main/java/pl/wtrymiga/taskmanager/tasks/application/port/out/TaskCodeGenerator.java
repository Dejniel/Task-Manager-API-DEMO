package pl.wtrymiga.taskmanager.tasks.application.port.out;

public interface TaskCodeGenerator {
	String nextCode(int year);
}
