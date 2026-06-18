package com.task.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.task.dto.AiTaskRequest;
import com.task.dto.AiTaskResponse;
import com.task.service.AiService;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {
	private final AiService aiService;

	@PostMapping("/generate-task-info")
	public ResponseEntity<AiTaskResponse> generateTaskInfo(@RequestBody AiTaskRequest request) {
		return ResponseEntity.ok(aiService.generateTaskInfo(request.getTitle()));
	}
}
