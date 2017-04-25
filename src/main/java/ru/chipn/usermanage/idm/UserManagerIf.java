package ru.chipn.usermanage.idm;

import org.picketlink.idm.model.basic.User;

import java.util.List;

/**
 * Интерфейс DAO работы с записями пользователей в хранилище (каталоге LDAP)
 */
public interface UserManagerIf {

    /**
     * возвращает список всех пользователей
     * @return
     */
    List<User> getAll();

    /**
     * Сохранение пользователя в каталоге
     * Новый пользователь записывается,
     * у существующего обновляется пароль
     */
    void saveUser();

    /**
     * Удаление пользователя из каталога
     */
    void dropUser();

    /**
     * Создание нового пользователя
     */
    void newUser();
    /**
     * Поиск пользователя по loginName в каталоге
     * @param loginName
     * @return
     */
    User getUser(String loginName);
}
