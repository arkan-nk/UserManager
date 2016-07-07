package ru.chipn.manage.login;

/**
 * Created by arkan on 28.06.2016.
 */
public enum ModuleEnum {
    CU_DN ("dc=cu,"),
    INV_DN ("dc=inv,"),
    DISP_DN ("dc=disp,"),
    REPAIR_DN ("dc=repair,"),
    MANAGE_DN ("dc=manage,");
    String txt;
    ModuleEnum(String t){
        txt = t;
    }
    String getTxt(){
        return txt;
    }
}
