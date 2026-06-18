package com.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.task.entity.Task;
import com.task.enums.Status;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
	List<Task> findByUserId(Long userId);

	List<Task> findByUserIdAndStatus(Long userId, Status status);
}
