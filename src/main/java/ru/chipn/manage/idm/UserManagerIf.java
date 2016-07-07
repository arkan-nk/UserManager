package ru.chipn.manage.idm;

import org.picketlink.idm.model.basic.User;

import java.util.List;

/**
 * Created by arkan on 01.07.2016.
 */
public interface UserManagerIf {
    List<User> getAll();
}
