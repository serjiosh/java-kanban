import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class DataIntegrityTest {
    private InMemoryTaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void testEpicSubtaskConsistency() {
        // Создаем эпик с подзадачами
        Epic epic = taskManager.createEpic("Тестовый эпик", "Описание эпика");
        Subtask subtask1 = taskManager.createSubtask("Подзадача 1", "Описание 1", epic.getId());
        Subtask subtask2 = taskManager.createSubtask("Подзадача 2", "Описание 2", epic.getId());
        
        // Проверяем, что эпик знает о своих подзадачах
        ArrayList<Subtask> epicSubtasks = taskManager.getSubtasksByEpic(epic.getId());
        assertEquals(2, epicSubtasks.size());
        assertTrue(epicSubtasks.contains(subtask1));
        assertTrue(epicSubtasks.contains(subtask2));
        
        // Удаляем подзадачу
        taskManager.deleteSubtask(subtask1.getId());
        
        // Проверяем, что эпик больше не знает об удаленной подзадаче
        epicSubtasks = taskManager.getSubtasksByEpic(epic.getId());
        assertEquals(1, epicSubtasks.size());
        assertFalse(epicSubtasks.contains(subtask1));
        assertTrue(epicSubtasks.contains(subtask2));
    }

    @Test
    void testEpicDeletionRemovesSubtasks() {
        // Создаем эпик с подзадачами
        Epic epic = taskManager.createEpic("Тестовый эпик", "Описание эпика");
        Subtask subtask1 = taskManager.createSubtask("Подзадача 1", "Описание 1", epic.getId());
        Subtask subtask2 = taskManager.createSubtask("Подзадача 2", "Описание 2", epic.getId());
        
        // Удаляем эпик
        taskManager.deleteEpic(epic.getId());
        
        // Проверяем, что подзадачи тоже удалены
        assertNull(taskManager.getSubtaskById(subtask1.getId()));
        assertNull(taskManager.getSubtaskById(subtask2.getId()));
        assertTrue(taskManager.getAllSubtasks().isEmpty());
    }

    @Test
    void testTaskModificationDoesNotBreakManager() {
        // Создаем задачу
        Task task = taskManager.createTask("Тестовая задача", "Описание");
        int originalId = task.getId();
        
        // Изменяем задачу через сеттеры
        task.setTitle("Новое название");
        task.setDescription("Новое описание");
        task.setStatus(TaskStatus.IN_PROGRESS);
        
        // Проверяем, что менеджер все еще может работать с задачей
        Task retrievedTask = taskManager.getTaskById(originalId);
        assertNotNull(retrievedTask);
        assertEquals("Новое название", retrievedTask.getTitle());
        assertEquals("Новое описание", retrievedTask.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, retrievedTask.getStatus());
    }

    @Test
    void testEpicStatusUpdate() {
        // Создаем эпик с подзадачами
        Epic epic = taskManager.createEpic("Тестовый эпик", "Описание эпика");
        assertEquals(TaskStatus.NEW, epic.getStatus());
        
        Subtask subtask1 = taskManager.createSubtask("Подзадача 1", "Описание 1", epic.getId());
        Subtask subtask2 = taskManager.createSubtask("Подзадача 2", "Описание 2", epic.getId());
        
        // Все подзадачи NEW - эпик должен быть NEW
        assertEquals(TaskStatus.NEW, epic.getStatus());
        
        // Одна подзадача IN_PROGRESS - эпик должен быть IN_PROGRESS
        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(subtask1);
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());
        
        // Все подзадачи DONE - эпик должен быть DONE
        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);
        assertEquals(TaskStatus.DONE, epic.getStatus());
    }

    @Test
    void testHistoryConsistencyAfterDeletion() {
        // Создаем задачи и просматриваем их
        Task task1 = taskManager.createTask("Задача 1", "Описание 1");
        Task task2 = taskManager.createTask("Задача 2", "Описание 2");
        Epic epic = taskManager.createEpic("Эпик", "Описание эпика");
        
        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getEpicById(epic.getId());
        
        // Проверяем, что все в истории
        ArrayList<Task> history = taskManager.getHistory();
        assertEquals(3, history.size());
        
        // Удаляем задачу
        taskManager.deleteTask(task1.getId());
        
        // Проверяем, что задача удалена из истории
        history = taskManager.getHistory();
        assertEquals(2, history.size());
        assertFalse(history.contains(task1));
        assertTrue(history.contains(task2));
        assertTrue(history.contains(epic));
    }

    @Test
    void testSubtaskEpicIdConsistency() {
        // Создаем эпик и подзадачу
        Epic epic = taskManager.createEpic("Тестовый эпик", "Описание эпика");
        Subtask subtask = taskManager.createSubtask("Подзадача", "Описание", epic.getId());
        
        // Проверяем, что подзадача знает свой эпик
        assertEquals(epic.getId(), subtask.getEpicId());
        
        // Проверяем, что эпик знает о подзадаче
        ArrayList<Subtask> epicSubtasks = taskManager.getSubtasksByEpic(epic.getId());
        assertEquals(1, epicSubtasks.size());
        assertEquals(subtask, epicSubtasks.get(0));
    }

    @Test
    void testEmptyEpicStatus() {
        // Создаем пустой эпик
        Epic epic = taskManager.createEpic("Пустой эпик", "Описание");
        assertEquals(TaskStatus.NEW, epic.getStatus());
        
        // Добавляем подзадачу
        taskManager.createSubtask("Подзадача", "Описание", epic.getId());
        assertEquals(TaskStatus.NEW, epic.getStatus());
        
        // Удаляем подзадачу
        ArrayList<Subtask> subtasks = taskManager.getSubtasksByEpic(epic.getId());
        if (!subtasks.isEmpty()) {
            taskManager.deleteSubtask(subtasks.get(0).getId());
        }
        
        // Эпик снова должен быть NEW
        assertEquals(TaskStatus.NEW, epic.getStatus());
    }
}
