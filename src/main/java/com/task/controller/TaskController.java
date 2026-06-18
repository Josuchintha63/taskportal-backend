package com.task.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.task.dto.TaskRequest;
import com.task.entity.Task;
import com.task.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
	private final TaskService taskService;
	

	@PostMapping
	public ResponseEntity<Task> createTask(Authentication auth, @Valid @RequestBody TaskRequest request) {
		return ResponseEntity.ok(taskService.createTask(auth.getName(), request));
	}

	@GetMapping
	public ResponseEntity<List<Task>> getAllTasks(Authentication auth) {
		return ResponseEntity.ok(taskService.getAllTasks(auth.getName()));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Task> updateTask(@PathVariable Long id, Authentication auth,
			@Valid @RequestBody TaskRequest request) {
		return ResponseEntity.ok(taskService.updateTask(id, auth.getName(), request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteTask(@PathVariable Long id, Authentication auth) {
		taskService.deleteTask(id, auth.getName());
		return ResponseEntity.ok("Task deleted successfully");
	}
}
