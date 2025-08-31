import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> tasks;           // Все обычные задачи
    private HashMap<Integer, Epic> epics;           // Все эпики
    private HashMap<Integer, Subtask> subtasks;     // Все подзадачи
    private int nextId;                             // Следующий доступный ID
    private final HistoryManager historyManager;    // Менеджер истории

    // Конструктор
    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.nextId = 1;
        this.historyManager = new InMemoryHistoryManager();
    }

    // Генерация нового ID
    private int generateId() {
        return nextId++;
    }

    // === МЕТОДЫ ДЛЯ ОБЫЧНЫХ ЗАДАЧ ===

    @Override
    public Task createTask(String title, String description) {
        Task task = new Task(generateId(), title, description, TaskStatus.NEW);
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    // === МЕТОДЫ ДЛЯ ЭПИКОВ ===

    @Override
    public Epic createEpic(String title, String description) {
        Epic epic = new Epic(generateId(), title, description);
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            updateEpicStatus(epic.getId());  // Пересчитываем статус
        }
    }

    @Override
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

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();  // Подзадачи тоже удаляем
    }

    // === МЕТОДЫ ДЛЯ ПОДЗАДАЧ ===

    @Override
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

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            updateEpicStatus(subtask.getEpicId());  // Пересчитываем статус эпика
        }
    }

    @Override
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

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
        // Обновляем статусы всех эпиков
        for (Epic epic : epics.values()) {
            epic.getSubtaskIds().clear();
            updateEpicStatus(epic.getId());
        }
    }

    // === ДОПОЛНИТЕЛЬНЫЕ МЕТОДЫ ===

    @Override
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

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
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
