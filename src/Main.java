public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
        System.out.println("=== СОЗДАНИЕ ЗАДАЧ ===");
        
        // Создаем две задачи
        Task task1 = manager.createTask("Помыть посуду", "Помыть всю грязную посуду на кухне");
        Task task2 = manager.createTask("Позвонить бабушке", "Узнать как дела у бабушки");
        
        // Создаем эпик с двумя подзадачами
        Epic epic1 = manager.createEpic("Организовать праздник", "Подготовить большой семейный праздник");
        Subtask subtask1 = manager.createSubtask("Заказать ресторан", "Найти и забронировать ресторан", epic1.getId());
        Subtask subtask2 = manager.createSubtask("Купить подарки", "Выбрать и купить подарки для всех", epic1.getId());
        
        // Создаем эпик с одной подзадачей
        Epic epic2 = manager.createEpic("Купить квартиру", "Найти и купить подходящую квартиру");
        Subtask subtask3 = manager.createSubtask("Найти агентство", "Выбрать надежное агентство недвижимости", epic2.getId());

        System.out.println("Задачи созданы!");
        
        // Выводим списки эпиков, задач и подзадач
        System.out.println("\n=== СПИСКИ ЗАДАЧ ===");
        System.out.println("Эпики: " + manager.getAllEpics());
        System.out.println("Задачи: " + manager.getAllTasks());
        System.out.println("Подзадачи: " + manager.getAllSubtasks());
        
        // Изменяем статусы созданных объектов
        System.out.println("\n=== ИЗМЕНЕНИЕ СТАТУСОВ ===");
        
        // Меняем статус обычной задачи
        task1.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateTask(task1);
        System.out.println("Статус задачи 1 изменен на IN_PROGRESS: " + task1);
        
        // Меняем статус подзадачи
        subtask1.setStatus(TaskStatus.DONE);
        manager.updateSubtask(subtask1);
        System.out.println("Статус подзадачи 1 изменен на DONE: " + subtask1);
        
        // Проверяем, что статус эпика рассчитался автоматически
        System.out.println("Статус эпика 1 после изменения подзадачи: " + manager.getEpicById(epic1.getId()));
        
        // Меняем статус второй подзадачи
        subtask2.setStatus(TaskStatus.DONE);
        manager.updateSubtask(subtask2);
        System.out.println("Статус подзадачи 2 изменен на DONE: " + subtask2);
        
        // Проверяем, что эпик стал DONE
        System.out.println("Статус эпика 1 после изменения всех подзадач: " + manager.getEpicById(epic1.getId()));
        
        // Выводим обновленные списки
        System.out.println("\n=== ОБНОВЛЕННЫЕ СПИСКИ ===");
        System.out.println("Эпики: " + manager.getAllEpics());
        System.out.println("Задачи: " + manager.getAllTasks());
        System.out.println("Подзадачи: " + manager.getAllSubtasks());
        
        // Получаем подзадачи конкретного эпика
        System.out.println("\n=== ПОДЗАДАЧИ ЭПИКА ===");
        System.out.println("Подзадачи эпика 1: " + manager.getSubtasksByEpic(epic1.getId()));
        System.out.println("Подзадачи эпика 2: " + manager.getSubtasksByEpic(epic2.getId()));
        
        // Удаляем одну из задач и один из эпиков
        System.out.println("\n=== УДАЛЕНИЕ ЗАДАЧ ===");
        manager.deleteTask(task1.getId());
        System.out.println("Задача 1 удалена");
        manager.deleteEpic(epic1.getId());
        System.out.println("Эпик 1 удален (вместе с подзадачами)");
        
        // Выводим финальные списки
        System.out.println("\n=== ФИНАЛЬНЫЕ СПИСКИ ===");
        System.out.println("Эпики: " + manager.getAllEpics());
        System.out.println("Задачи: " + manager.getAllTasks());
        System.out.println("Подзадачи: " + manager.getAllSubtasks());
        
        // Дополнительные тесты граничных случаев
        System.out.println("\n=== ДОПОЛНИТЕЛЬНЫЕ ТЕСТЫ ===");
        
        // Тест получения несуществующей задачи
        Task nonExistentTask = manager.getTaskById(999);
        System.out.println("Получение несуществующей задачи: " + nonExistentTask);
        
        // Тест обновления несуществующей задачи
        Task fakeTask = new Task(999, "Фейковая задача", "Не существует", TaskStatus.DONE);
        manager.updateTask(fakeTask);
        System.out.println("После попытки обновления несуществующей задачи: " + manager.getTaskById(999));
        
        // Тест автоматического пересчета статуса эпика при изменении подзадачи
        System.out.println("\n=== ТЕСТ АВТОМАТИЧЕСКОГО ПЕРЕСЧЕТА СТАТУСА ===");
        Subtask remainingSubtask = manager.getSubtaskById(7); // Подзадача эпика 2
        System.out.println("Текущий статус эпика 2: " + manager.getEpicById(epic2.getId()));
        
        remainingSubtask.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubtask(remainingSubtask);
        System.out.println("После изменения подзадачи на IN_PROGRESS, статус эпика 2: " + manager.getEpicById(epic2.getId()));
        
        remainingSubtask.setStatus(TaskStatus.DONE);
        manager.updateSubtask(remainingSubtask);
        System.out.println("После изменения подзадачи на DONE, статус эпика 2: " + manager.getEpicById(epic2.getId()));
        
        // Финальный вывод
        System.out.println("\n=== ИТОГОВЫЕ СПИСКИ ===");
        System.out.println("Эпики: " + manager.getAllEpics());
        System.out.println("Задачи: " + manager.getAllTasks());
        System.out.println("Подзадачи: " + manager.getAllSubtasks());
    }
}
