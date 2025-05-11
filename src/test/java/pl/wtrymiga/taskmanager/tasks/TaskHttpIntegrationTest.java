package pl.wtrymiga.taskmanager.tasks;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import pl.wtrymiga.taskmanager.tasks.domain.TaskVisibility;
import pl.wtrymiga.taskmanager.web.tasks.dto.TaskResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class TaskHttpIntegrationTest {
	@Autowired
	WebTestClient webTestClient;

	@Test
	void createAndComplete() {
		long taskId = webTestClient.post().uri("/tasks").header("X-User-Id", "u")
				.bodyValue(new pl.wtrymiga.taskmanager.web.tasks.dto.TaskCreateRequest("t", "d", TaskVisibility.PUBLIC,
						null))
				.exchange().expectStatus().isOk().expectBody(TaskResponse.class).returnResult().getResponseBody().id();
		webTestClient.put().uri("/tasks/{id}/complete", taskId).header("X-User-Id", "u").exchange().expectStatus()
				.isOk();
	}
}
