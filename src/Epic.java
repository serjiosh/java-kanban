import java.util.ArrayList;
public class Epic extends Task {
    private ArrayList<Integer> subtaskIds;  // Список ID подзадач

    // Конструктор
    public Epic(int id, String title, String description) {
        super(id, title, description, TaskStatus.NEW);  // Эпик всегда начинается со статуса NEW
        this.subtaskIds = new ArrayList<>();
    }

    // Геттеры и сеттеры
    public ArrayList<Integer> getSubtaskIds() {
        return new ArrayList<>(subtaskIds);  // Возвращаем копию, чтобы не могли изменить извне
    }

    public void addSubtask(int subtaskId) {
        if (!subtaskIds.contains(subtaskId)) {
            subtaskIds.add(subtaskId);
        }
    }

    public void removeSubtask(int subtaskId) {
        subtaskIds.remove(Integer.valueOf(subtaskId));
    }

    // Проверяем, есть ли подзадачи
    public boolean hasSubtasks() {
        return !subtaskIds.isEmpty();
    }

    @Override
    public String toString() {
        return "Epic{id=" + getId() + ", title='" + getTitle() + "', status=" + getStatus() +
                ", subtasks=" + subtaskIds.size() + "}";
    }
}