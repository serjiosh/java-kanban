import java.util.ArrayList;

public interface HistoryManager {
    // Добавить задачу в историю просмотров
    void add(Task task);

    // Удалить задачу из истории просмотров
    void remove(int id);

    // Получить историю просмотров
    ArrayList<Task> getHistory();
}
