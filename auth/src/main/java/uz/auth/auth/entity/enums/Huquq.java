package uz.auth.auth.entity.enums;

public enum Huquq {
    // РАЗРЕШЕНИЯ ДЛЯ АДМИНИСТРАТОРА
    ADD_USER,        // Добавление пользователя
    EDIT_USER,       // Редактирование пользователя
    DELETE_USER,     // Удаление пользователя

    ADD_DIVISION,    // Добавление подразделения
    EDIT_DIVISION,   // Редактирование подразделения
    DELETE_DIVISION, // Удаление подразделения

    // РАЗРЕШЕНИЯ ДЛЯ ПОЛЬЗОВАТЕЛЯ И АДМИНИСТРАТОРА
    SEND_FILE,       // Отправка файла
    DELETE_FILE,     // Удаление файла
    VIEW_STATISTIKA, // Просмотр статистики
    VIEW_USER,       // Просмотр информации о пользователе
}
