import java.util.ArrayList;

public interface HistoryManager {
    // Добавить задачу в историю просмотров
    void add(Task task);
    
    // Получить историю просмотров (последние 10 задач)
    ArrayList<Task> getHistory();
}
