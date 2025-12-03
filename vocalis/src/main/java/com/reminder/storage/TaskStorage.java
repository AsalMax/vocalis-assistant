package com.reminder.storage;
import com.reminder.model.Task;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class TaskStorage {

    private static final String URL = "jdbc:postgresql://localhost:5432/reminder_db";
    private static final String USER = "postgres";
    private static final String PASS = "Shaxina0930!";  

    static {
        createTable();
    }

    private static void createTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS tasks (
                id VARCHAR(50) PRIMARY KEY,
                title VARCHAR(200) NOT NULL,
                task_date DATE,
                task_time TIME,
                description TEXT,
                synced BOOLEAN DEFAULT FALSE,
                google_event_id VARCHAR(100)
            );
            """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("PostgreSQL connected! Table 'tasks' ready.");
        } catch (SQLException e) {
            System.err.println("Cannot connect to PostgreSQL. Check password or start server!");
            e.printStackTrace();
        }
    }

    // SAVE TASK (add or update)
    public static void saveTask(Task task) {
        String sql = """
            INSERT INTO tasks (id, title, task_date, task_time, description, synced, google_event_id)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            ON CONFLICT (id) DO UPDATE SET
                title = EXCLUDED.title,
                task_date = EXCLUDED.task_date,
                task_time = EXCLUDED.task_time,
                description = EXCLUDED.description
            """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, task.getId());
            ps.setString(2, task.getTitle());
            ps.setObject(3, task.getDate());
            ps.setObject(4, task.getTime());
            ps.setString(5, task.getDescription());
            ps.setBoolean(6, task.isSyncedWithGoogle());
            ps.setString(7, task.getGoogleEventId());

            ps.executeUpdate();
            System.out.println("Task saved to PostgreSQL: " + task.getTitle());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE TASK
    public static void deleteTask(String id) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // LOAD ALL TASKS FROM POSTGRES
    public static void loadTasks() {
        List<Task> loaded = new ArrayList<>();
        String sql = "SELECT * FROM tasks ORDER BY task_date, task_time";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Task t = new Task(
                    rs.getString("id"),
                    rs.getString("title"),
                    rs.getObject("task_date", LocalDate.class),
                    rs.getObject("task_time", LocalTime.class),
                    rs.getString("description"),
                    rs.getBoolean("synced"),
                    rs.getString("google_event_id")
                );
                loaded.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        com.reminder.manager.TaskManager.getInstance().loadTasks(loaded);
        System.out.println("Loaded " + loaded.size() + " tasks from PostgreSQL");
    }
}