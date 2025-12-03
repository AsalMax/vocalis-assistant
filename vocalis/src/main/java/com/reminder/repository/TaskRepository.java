package com.reminder.repository;
import com.reminder.model.Task;
import java.util.List;

public interface TaskRepository {
    List<Task> getAllTasks();
    void saveTask(Task task);
    void updateTask(Task task);
    void deleteTask(Task task);
}
