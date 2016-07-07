package ru.chipn.manage.login;

/**
 * Created by arkan on 28.06.2016.
 */
public enum ConfigurationEnum {
    LDAP_URL("ldap://vm-uniform.chip-n.ru:389"),
    ROOT_DN ("dc=chipn,dc=ru"),
    USERS_OU("ou=Users,"),
    BASE_DN("dc=a2,"),
    ROLES_OU ("ou=Roles,"),
    GROUPS_OU("ou=Groups,");
    String txt;
    ConfigurationEnum(String t){
        txt = t;
    }
    String getTxt(){
        return txt;
    }
}
