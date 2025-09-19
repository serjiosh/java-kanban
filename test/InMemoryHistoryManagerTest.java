import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class InMemoryHistoryManagerTest {
    private InMemoryHistoryManager historyManager;
    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        task1 = new Task(1, "Задача 1", "Описание 1", TaskStatus.NEW);
        task2 = new Task(2, "Задача 2", "Описание 2", TaskStatus.IN_PROGRESS);
        task3 = new Task(3, "Задача 3", "Описание 3", TaskStatus.DONE);
    }

    @Test
    void testAddTask() {
        historyManager.add(task1);
        ArrayList<Task> history = historyManager.getHistory();
        
        assertEquals(1, history.size());
        assertEquals(task1, history.get(0));
    }

    @Test
    void testAddMultipleTasks() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        
        ArrayList<Task> history = historyManager.getHistory();
        assertEquals(3, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
        assertEquals(task3, history.get(2));
    }

    @Test
    void testAddDuplicateTask() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1); // Повторное добавление
        
        ArrayList<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task2, history.get(0));
        assertEquals(task1, history.get(1)); // task1 должен быть в конце
    }

    @Test
    void testRemoveTask() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        
        historyManager.remove(task2.getId());
        
        ArrayList<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task3, history.get(1));
    }

    @Test
    void testRemoveNonExistentTask() {
        historyManager.add(task1);
        historyManager.add(task2);
        
        historyManager.remove(999); // Несуществующий ID
        
        ArrayList<Task> history = historyManager.getHistory();
        assertEquals(2, history.size()); // Размер не изменился
    }

    @Test
    void testRemoveFromEmptyHistory() {
        historyManager.remove(task1.getId());
        
        ArrayList<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty());
    }

    @Test
    void testAddNullTask() {
        historyManager.add(null);
        
        ArrayList<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty());
    }

    @Test
    void testHistoryOrder() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task1); // Повторное добавление task1
        
        ArrayList<Task> history = historyManager.getHistory();
        assertEquals(3, history.size());
        assertEquals(task2, history.get(0));
        assertEquals(task3, history.get(1));
        assertEquals(task1, history.get(2)); // task1 должен быть в конце
    }

    @Test
    void testRemoveFirstTask() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        
        historyManager.remove(task1.getId());
        
        ArrayList<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task2, history.get(0));
        assertEquals(task3, history.get(1));
    }

    @Test
    void testRemoveLastTask() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        
        historyManager.remove(task3.getId());
        
        ArrayList<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
    }

    @Test
    void testRemoveMiddleTask() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        
        historyManager.remove(task2.getId());
        
        ArrayList<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task3, history.get(1));
    }
}
