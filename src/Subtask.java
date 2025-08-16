public class Subtask extends Task {
    private int epicId;  // ID эпика, к которому принадлежит подзадача

    // Конструктор
    public Subtask(int id, String title, String description, TaskStatus status, int epicId) {
        super(id, title, description, status);  // Передаем статус как параметр
        this.epicId = epicId;
    }

    // Геттеры и сеттеры
    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{id=" + getId() + ", title='" + getTitle() + "', status=" + getStatus() +
                ", epicId=" + epicId + "}";
    }
}