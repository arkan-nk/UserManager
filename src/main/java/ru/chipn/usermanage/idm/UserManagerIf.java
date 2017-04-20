package ru.chipn.usermanage.idm;

import org.picketlink.idm.model.basic.User;

import java.util.List;

/**
 * Интерфейс DAO работы с записями пользователей в хранилище (каталоге LDAP)
 */
public interface UserManagerIf {
    List<User> getAll();
    void saveUser();
    void dropUser();
    void newUser();
    User getUser(String loginName);
}
