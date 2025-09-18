public class Main {
    public static void main(String[] args) {
        // Используем утилитарный класс для создания менеджера
        TaskManager manager = Managers.getDefault();
        
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
        
        // Тестируем историю просмотров
        System.out.println("\n=== ТЕСТИРОВАНИЕ ИСТОРИИ ПРОСМОТРОВ ===");
        System.out.println("История до просмотров: " + manager.getHistory());
        
        // Просматриваем задачи - это должно добавить их в историю
        System.out.println("\nПросматриваем задачу 1:");
        Task viewedTask1 = manager.getTaskById(task1.getId());
        System.out.println("История после просмотра задачи 1: " + manager.getHistory());
        
        System.out.println("\nПросматриваем эпик 1:");
        Epic viewedEpic1 = manager.getEpicById(epic1.getId());
        System.out.println("История после просмотра эпика 1: " + manager.getHistory());
        
        System.out.println("\nПросматриваем подзадачу 1:");
        Subtask viewedSubtask1 = manager.getSubtaskById(subtask1.getId());
        System.out.println("История после просмотра подзадачи 1: " + manager.getHistory());
        
        System.out.println("\nПросматриваем задачу 2:");
        Task viewedTask2 = manager.getTaskById(task2.getId());
        System.out.println("История после просмотра задачи 2: " + manager.getHistory());
        
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
        
        // Финальная история просмотров
        System.out.println("\n=== ФИНАЛЬНАЯ ИСТОРИЯ ПРОСМОТРОВ ===");
        System.out.println("История: " + manager.getHistory());
        
        // Дополнительный пользовательский сценарий
        System.out.println("\n=== ДОПОЛНИТЕЛЬНЫЙ СЦЕНАРИЙ ===");
        
        // Создаем новые задачи для тестирования
        Task newTask1 = manager.createTask("Новая задача 1", "Описание новой задачи 1");
        Task newTask2 = manager.createTask("Новая задача 2", "Описание новой задачи 2");
        
        Epic newEpic1 = manager.createEpic("Новый эпик с подзадачами", "Эпик с тремя подзадачами");
        Subtask newSubtask1 = manager.createSubtask("Подзадача 1", "Описание подзадачи 1", newEpic1.getId());
        Subtask newSubtask2 = manager.createSubtask("Подзадача 2", "Описание подзадачи 2", newEpic1.getId());
        Subtask newSubtask3 = manager.createSubtask("Подзадача 3", "Описание подзадачи 3", newEpic1.getId());
        
        Epic newEpic2 = manager.createEpic("Эпик без подзадач", "Эпик без подзадач");
        
        System.out.println("Созданы новые задачи и эпики");
        
        // Просматриваем задачи в разном порядке
        System.out.println("\n=== ПРОСМОТР В РАЗНОМ ПОРЯДКЕ ===");
        
        manager.getTaskById(newTask2.getId());
        System.out.println("История после просмотра задачи 2: " + manager.getHistory());
        
        manager.getEpicById(newEpic1.getId());
        System.out.println("История после просмотра эпика 1: " + manager.getHistory());
        
        manager.getSubtaskById(newSubtask1.getId());
        System.out.println("История после просмотра подзадачи 1: " + manager.getHistory());
        
        manager.getTaskById(newTask1.getId());
        System.out.println("История после просмотра задачи 1: " + manager.getHistory());
        
        // Повторный просмотр - должен переместить в конец
        manager.getTaskById(newTask2.getId());
        System.out.println("История после повторного просмотра задачи 2: " + manager.getHistory());
        
        // Удаляем задачу, которая есть в истории
        System.out.println("\n=== УДАЛЕНИЕ ЗАДАЧИ ИЗ ИСТОРИИ ===");
        manager.deleteTask(newTask1.getId());
        System.out.println("История после удаления задачи 1: " + manager.getHistory());
        
        // Удаляем эпик с подзадачами
        System.out.println("\n=== УДАЛЕНИЕ ЭПИКА С ПОДЗАДАЧАМИ ===");
        manager.deleteEpic(newEpic1.getId());
        System.out.println("История после удаления эпика 1 и его подзадач: " + manager.getHistory());
        
        System.out.println("\n=== ИТОГОВАЯ ИСТОРИЯ ===");
        System.out.println("Финальная история: " + manager.getHistory());
    }
}
