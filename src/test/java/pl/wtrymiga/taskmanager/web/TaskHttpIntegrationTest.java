package pl.wtrymiga.taskmanager.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import pl.wtrymiga.taskmanager.tasks.domain.TaskVisibility;
import pl.wtrymiga.taskmanager.web.tasks.dto.TaskCreateRequest;
import pl.wtrymiga.taskmanager.web.tasks.dto.TaskResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class TaskHttpIntegrationTest {
	@Autowired
	WebTestClient client;

	@Test
	void fullFlow() {
		long id = client.post().uri("/tasks").header("X-User-Id", "u")
				.bodyValue(new TaskCreateRequest("t", "d", TaskVisibility.PUBLIC, null)).exchange().expectStatus()
				.isOk().expectBody(TaskResponse.class).returnResult().getResponseBody().id();
		client.put().uri("/tasks/{id}/complete", id).header("X-User-Id", "u").exchange().expectStatus().isOk();
		client.get().uri("/tasks/{id}/changes", id).header("X-User-Id", "u").exchange().expectStatus().isOk();
		client.get().uri("/tasks?limit=5").header("X-User-Id", "u").exchange().expectStatus().isOk();
		client.get().uri("/tasks/export?format=csv").header("X-User-Id", "u").exchange().expectStatus().isOk();
	}
}
