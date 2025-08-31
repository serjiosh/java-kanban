import java.util.ArrayList;
import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager {
    private final LinkedList<Task> history;
    private static final int MAX_HISTORY_SIZE = 10;
    
    public InMemoryHistoryManager() {
        this.history = new LinkedList<>();
    }
    
    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        
        // Удаляем задачу, если она уже есть в истории (чтобы переместить в конец)
        history.remove(task);
        
        // Добавляем задачу в конец истории
        history.addLast(task);
        
        // Если история превышает максимальный размер, удаляем самый старый элемент
        if (history.size() > MAX_HISTORY_SIZE) {
            history.removeFirst();
        }
    }
    
    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
