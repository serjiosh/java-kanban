import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks;           // Все обычные задачи
    private HashMap<Integer, Epic> epics;           // Все эпики
    private HashMap<Integer, Subtask> subtasks;     // Все подзадачи
    private int nextId;                             // Следующий доступный ID

    // Конструктор
    public TaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.nextId = 1;
    }

    // Генерация нового ID
    private int generateId() {
        return nextId++;
    }

    // === МЕТОДЫ ДЛЯ ОБЫЧНЫХ ЗАДАЧ ===

    // Создание задачи
    public Task createTask(String title, String description) {
        Task task = new Task(generateId(), title, description, TaskStatus.NEW);
        tasks.put(task.getId(), task);
        return task;
    }

    // Получение всех задач
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    // Получение задачи по ID
    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    // Обновление задачи
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    // Удаление задачи
    public void deleteTask(int id) {
        tasks.remove(id);
    }

    // Удаление всех задач
    public void deleteAllTasks() {
        tasks.clear();
    }

    // === МЕТОДЫ ДЛЯ ЭПИКОВ ===

    // Создание эпика
    public Epic createEpic(String title, String description) {
        Epic epic = new Epic(generateId(), title, description);
        epics.put(epic.getId(), epic);
        return epic;
    }

    // Получение всех эпиков
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    // Получение эпика по ID
    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    // Обновление эпика
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            updateEpicStatus(epic.getId());  // Пересчитываем статус
        }
    }

    // Удаление эпика
    public void deleteEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            // Удаляем все подзадачи эпика
            for (Integer subtaskId : epic.getSubtaskIds()) {
                subtasks.remove(subtaskId);
            }
            epics.remove(id);
        }
    }

    // Удаление всех эпиков
    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();  // Подзадачи тоже удаляем
    }

    // === МЕТОДЫ ДЛЯ ПОДЗАДАЧ ===

    // Создание подзадачи
    public Subtask createSubtask(String title, String description, int epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            Subtask subtask = new Subtask(generateId(), title, description, TaskStatus.NEW, epicId);
            subtasks.put(subtask.getId(), subtask);
            epic.addSubtask(subtask.getId());
            updateEpicStatus(epicId);  // Пересчитываем статус эпика
            return subtask;
        }
        return null;
    }

    // Получение всех подзадач
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    // Получение подзадачи по ID
    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    // Обновление подзадачи
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            updateEpicStatus(subtask.getEpicId());  // Пересчитываем статус эпика
        }
    }

    // Удаление подзадачи
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtask(id);
                updateEpicStatus(epic.getId());
            }
            subtasks.remove(id);
        }
    }

    // Удаление всех подзадач
    public void deleteAllSubtasks() {
        subtasks.clear();
        // Обновляем статусы всех эпиков
        for (Epic epic : epics.values()) {
            epic.getSubtaskIds().clear();
            updateEpicStatus(epic.getId());
        }
    }

    // === ДОПОЛНИТЕЛЬНЫЕ МЕТОДЫ ===

    // Получение всех подзадач эпика
    public ArrayList<Subtask> getSubtasksByEpic(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            ArrayList<Subtask> result = new ArrayList<>();
            for (Integer subtaskId : epic.getSubtaskIds()) {
                Subtask subtask = subtasks.get(subtaskId);
                if (subtask != null) {
                    result.add(subtask);
                }
            }
            return result;
        }
        return new ArrayList<>();
    }

    // Обновление статуса эпика
    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            ArrayList<Subtask> epicSubtasks = getSubtasksByEpic(epicId);

            if (epicSubtasks.isEmpty()) {
                epic.setStatus(TaskStatus.NEW);
            } else {
                boolean allDone = true;
                boolean allNew = true;

                for (Subtask subtask : epicSubtasks) {
                    if (subtask.getStatus() != TaskStatus.DONE) {
                        allDone = false;
                    }
                    if (subtask.getStatus() != TaskStatus.NEW) {
                        allNew = false;
                    }
                }

                if (allDone) {
                    epic.setStatus(TaskStatus.DONE);
                } else if (allNew) {
                    epic.setStatus(TaskStatus.NEW);
                } else {
                    epic.setStatus(TaskStatus.IN_PROGRESS);
                }
            }
        }
    }
}