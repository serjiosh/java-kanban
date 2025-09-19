import java.util.ArrayList;

public interface TaskManager {
    // === МЕТОДЫ ДЛЯ ОБЫЧНЫХ ЗАДАЧ ===

    // Создание задачи
    Task createTask(String title, String description);

    // Получение всех задач
    ArrayList<Task> getAllTasks();

    // Получение задачи по ID
    Task getTaskById(int id);

    // Обновление задачи
    void updateTask(Task task);

    // Удаление задачи
    void deleteTask(int id);

    // Удаление всех задач
    void deleteAllTasks();

    // === МЕТОДЫ ДЛЯ ЭПИКОВ ===

    // Создание эпика
    Epic createEpic(String title, String description);

    // Получение всех эпиков
    ArrayList<Epic> getAllEpics();

    // Получение эпика по ID
    Epic getEpicById(int id);

    // Обновление эпика
    void updateEpic(Epic epic);

    // Удаление эпика
    void deleteEpic(int id);

    // Удаление всех эпиков
    void deleteAllEpics();

    // === МЕТОДЫ ДЛЯ ПОДЗАДАЧ ===

    // Создание подзадачи
    Subtask createSubtask(String title, String description, int epicId);

    // Получение всех подзадач
    ArrayList<Subtask> getAllSubtasks();

    // Получение подзадачи по ID
    Subtask getSubtaskById(int id);

    // Обновление подзадачи
    void updateSubtask(Subtask subtask);

    // Удаление подзадачи
    void deleteSubtask(int id);

    // Удаление всех подзадач
    void deleteAllSubtasks();

    // === ДОПОЛНИТЕЛЬНЫЕ МЕТОДЫ ===

    // Получение всех подзадач эпика
    ArrayList<Subtask> getSubtasksByEpic(int epicId);

    // Получение истории просмотров
    ArrayList<Task> getHistory();
}