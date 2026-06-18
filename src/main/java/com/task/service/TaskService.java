package com.task.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.task.dto.TaskRequest;
import com.task.entity.Task;
import com.task.entity.User;
import com.task.exception.ResourceNotFoundException;
import com.task.repository.TaskRepository;
import com.task.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public Task createTask(String username, TaskRequest request) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority())
                .status(request.getStatus())
                .dueDate(request.getDueDate())
                .user(user)
                .build();

        return taskRepository.save(task);
    }

    public List<Task> getAllTasks(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return taskRepository.findByUserId(user.getId());
    }

    public Task updateTask(Long taskId,
                           String username,
                           TaskRequest request) {

        Task task = getOwnedTask(taskId, username);

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setStatus(request.getStatus());
        task.setDueDate(request.getDueDate());

        return taskRepository.save(task);
    }

    public void deleteTask(Long taskId, String username) {

        Task task = getOwnedTask(taskId, username);
        taskRepository.delete(task);
    }

    private Task getOwnedTask(Long taskId, String username) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found"));

        if (!task.getUser().getUsername().equals(username)) {
            throw new SecurityException(
                    "Not authorized to modify this task");
        }

        return task;
    }
}