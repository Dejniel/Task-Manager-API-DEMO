package pl.wtrymiga.taskmanager.web;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import pl.wtrymiga.taskmanager.tasks.domain.TaskVisibility;
import pl.wtrymiga.taskmanager.web.tasks.dto.TaskCreateRequest;
import pl.wtrymiga.taskmanager.web.tasks.dto.TaskPatchRequest;
import pl.wtrymiga.taskmanager.web.tasks.dto.TaskResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class TaskOptimisticLockHttpTest {

	@Autowired
	WebTestClient client;

	@Test
	void staleIfMatchReturns409() {
		TaskResponse created = client.post().uri("/tasks").header("X-User-Id", "u")
				.bodyValue(new TaskCreateRequest("first", "d", TaskVisibility.PUBLIC, null)).exchange().expectStatus()
				.isOk().expectBody(TaskResponse.class).returnResult().getResponseBody();

		long id = created.id();
		long v0 = created.version();
		assertThat(v0).isZero();

		TaskResponse patched = client.patch().uri("/tasks/{id}", id).header("X-User-Id", "u")
				.header("If-Match", String.valueOf(v0)).bodyValue(new TaskPatchRequest("second", null, null)).exchange()
				.expectStatus().isOk().expectBody(TaskResponse.class).returnResult().getResponseBody();

		assertThat(patched.version()).isEqualTo(v0 + 1);

		client.patch().uri("/tasks/{id}", id).header("X-User-Id", "u").header("If-Match", String.valueOf(v0))

				.bodyValue(new TaskPatchRequest("third", null, null)).exchange().expectStatus()
				.isEqualTo(HttpStatus.CONFLICT);
	}
}
