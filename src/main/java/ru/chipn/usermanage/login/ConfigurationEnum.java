package ru.chipn.usermanage.login;

/**
 * Created by arkan on 28.06.2016.
 */
public enum ConfigurationEnum {
    LDAP_URL("ldap://localhost:10389"),
    ROOT_DN ("dc=a2,dc=jboss,dc=org"),
    USERS_OU("ou=Users,"),
    BASE_DN("dc=work,"),
    ROLES_OU ("ou=Roles,"),
    GROUPS_OU("ou=Groups,");
    String txt;
    ConfigurationEnum(String t){
        txt = t;
    }
    public String getTxt(){
        return txt;
    }
}
