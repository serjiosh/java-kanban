# Настройка JUnit 5 в проекте Java Kanban

## Шаг 1: Создание папки test

1. В IntelliJ IDEA кликните правой кнопкой мыши на папку `test`
2. Выберите `Mark Directory as → Test Sources`

## Шаг 2: Добавление JUnit 5

### Вариант 1: Через IntelliJ IDEA (рекомендуется)

1. Откройте любой класс (например, `Task.java`)
2. Нажмите `Ctrl+Shift+T` (Windows/Linux) или `Shift+Cmd+T` (macOS)
3. В выпадающем меню выберите `Create New Test`
4. В поле `Testing library` выберите `JUnit5`
5. Нажмите `Fix` для автоматической загрузки библиотек
6. Выберите папку `lib` для скачивания библиотек
7. Нажмите `OK`

### Вариант 2: Ручное добавление

1. Скачайте JUnit 5 с официального сайта: https://junit.org/junit5/
2. Создайте папку `lib` в корне проекта
3. Поместите скачанные JAR файлы в папку `lib`
4. В IntelliJ IDEA добавьте библиотеки в `Project Structure → Libraries`

## Шаг 3: Проверка настройки

После настройки JUnit 5:

1. Откройте любой тест (например, `TaskTest.java`)
2. Убедитесь, что импорты работают:
   ```java
   import org.junit.jupiter.api.Test;
   import static org.junit.jupiter.api.Assertions.*;
   ```
3. Запустите тест правой кнопкой мыши → `Run 'TaskTest'`

## Структура тестов

```
test/
├── TaskTest.java           # Тесты для класса Task
├── EpicTest.java           # Тесты для класса Epic
├── SubtaskTest.java        # Тесты для класса Subtask
├── InMemoryTaskManagerTest.java    # Тесты для TaskManager
├── InMemoryHistoryManagerTest.java # Тесты для HistoryManager
└── ManagersTest.java       # Тесты для утилитарного класса
```

## Примеры тестов

### Тест создания задачи
```java
@Test
void testCreateTask() {
    TaskManager manager = Managers.getDefault();
    Task task = manager.createTask("Test", "Description");
    
    assertNotNull(task, "Задача должна быть создана");
    assertEquals("Test", task.getTitle(), "Название должно совпадать");
}
```

### Тест истории просмотров
```java
@Test
void testHistory() {
    TaskManager manager = Managers.getDefault();
    Task task = manager.createTask("Test", "Description");
    
    // Просматриваем задачу
    manager.getTaskById(task.getId());
    
    ArrayList<Task> history = manager.getHistory();
    assertEquals(1, history.size(), "История должна содержать 1 элемент");
    assertEquals(task, history.get(0), "Элемент истории должен совпадать с задачей");
}
```

## Запуск всех тестов

1. Правой кнопкой мыши на папку `test`
2. Выберите `Run 'All Tests'`

## Покрытие тестами

Основные сценарии для тестирования:

- ✅ Создание, чтение, обновление, удаление задач
- ✅ Автоматический расчет статуса эпика
- ✅ История просмотров (добавление, ограничение размера)
- ✅ Равенство задач по ID
- ✅ Валидация данных
- ✅ Граничные случаи

## Устранение проблем

### Ошибка "Cannot resolve symbol 'Test'"
- Проверьте, что JUnit 5 добавлен в зависимости
- Убедитесь, что папка `test` отмечена как Test Sources

### Ошибка "No tests found"
- Проверьте, что методы тестов помечены аннотацией `@Test`
- Убедитесь, что методы публичные и не статические

### Ошибка "Class not found"
- Проверьте, что JAR файлы JUnit находятся в папке `lib`
- Убедитесь, что библиотеки добавлены в classpath проекта
