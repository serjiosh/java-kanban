import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    // Узел двусвязного списка
    private static class Node {
        Task data;
        Node prev;
        Node next;
        
        Node(Node prev, Task data, Node next) {
            this.prev = prev;
            this.data = data;
            this.next = next;
        }
    }
    
    private final Map<Integer, Node> nodeMap; // id -> узел для быстрого доступа
    private Node first; // Первый узел
    private Node last;  // Последний узел
    
    public InMemoryHistoryManager() {
        this.nodeMap = new HashMap<>();
        this.first = null;
        this.last = null;
    }
    
    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        
        int taskId = task.getId();
        
        // Если задача уже есть в истории, удаляем её
        if (nodeMap.containsKey(taskId)) {
            removeNode(nodeMap.get(taskId));
        }
        
        // Добавляем задачу в конец списка
        linkLast(task);
    }
    
    @Override
    public void remove(int id) {
        Node node = nodeMap.get(id);
        if (node != null) {
            removeNode(node);
        }
    }
    
    @Override
    public ArrayList<Task> getHistory() {
        return getTasks();
    }
    
    // Добавляет задачу в конец двусвязного списка
    private void linkLast(Task task) {
        Node newNode = new Node(last, task, null);
        
        if (last == null) {
            first = newNode;
        } else {
            last.next = newNode;
        }
        last = newNode;
        
        // Обновляем HashMap
        nodeMap.put(task.getId(), newNode);
    }
    
    // Удаляет узел из двусвязного списка
    private void removeNode(Node node) {
        if (node == null) {
            return;
        }
        
        // Обновляем связи
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            first = node.next;
        }
        
        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            last = node.prev;
        }
        
        // Удаляем из HashMap
        nodeMap.remove(node.data.getId());
    }
    
    // Собирает все задачи из двусвязного списка в ArrayList
    private ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node current = first;
        
        while (current != null) {
            tasks.add(current.data);
            current = current.next;
        }
        
        return tasks;
    }
}
