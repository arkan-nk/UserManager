package ru.chipn.usermanage.login;

/**
 * Created by arkan on 28.06.2016.
 */
public enum ModuleEnum {
    CU_DN ("cu"),
    INV_DN ("inv"),
    DISP_DN ("disp"),
    REPAIR_DN ("repair"),
    MANAGE_DN ("manage");
    String txt;
    ModuleEnum(String t){
        txt = t;
    }
    public String getTxt(){
        return "dc="+txt+",";
    }
    public String getModule(){
        return txt;
    }
}
