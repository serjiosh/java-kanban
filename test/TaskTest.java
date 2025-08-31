import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {
    
    @Test
    void testTaskEquality() {
        Task task1 = new Task(1, "Test Task", "Test Description", TaskStatus.NEW);
        Task task2 = new Task(1, "Different Title", "Different Description", TaskStatus.DONE);
        
        // Задачи с одинаковым ID должны быть равны
        assertEquals(task1, task2, "Задачи с одинаковым ID должны быть равны");
        assertEquals(task1.hashCode(), task2.hashCode(), "HashCode должен быть одинаковым для равных задач");
    }
    
    @Test
    void testTaskInequality() {
        Task task1 = new Task(1, "Test Task", "Test Description", TaskStatus.NEW);
        Task task2 = new Task(2, "Test Task", "Test Description", TaskStatus.NEW);
        
        // Задачи с разными ID не должны быть равны
        assertNotEquals(task1, task2, "Задачи с разными ID не должны быть равны");
    }
    
    @Test
    void testTaskFields() {
        Task task = new Task(1, "Test Task", "Test Description", TaskStatus.IN_PROGRESS);
        
        assertEquals(1, task.getId(), "ID должен быть равен 1");
        assertEquals("Test Task", task.getTitle(), "Title должен быть 'Test Task'");
        assertEquals("Test Description", task.getDescription(), "Description должен быть 'Test Description'");
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus(), "Status должен быть IN_PROGRESS");
    }
    
    @Test
    void testTaskSetters() {
        Task task = new Task(1, "Original Title", "Original Description", TaskStatus.NEW);
        
        task.setTitle("New Title");
        task.setDescription("New Description");
        task.setStatus(TaskStatus.DONE);
        
        assertEquals("New Title", task.getTitle(), "Title должен измениться на 'New Title'");
        assertEquals("New Description", task.getDescription(), "Description должен измениться на 'New Description'");
        assertEquals(TaskStatus.DONE, task.getStatus(), "Status должен измениться на DONE");
    }
}
