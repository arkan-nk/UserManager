package ru.chipn.usermanage.login;

/**
 * Created by arkan on 28.06.2016.
 */
public enum ModuleEnum {
    CU_DN ("cu", Resources.getCaption("cuName")),
    INV_DN ("inv", Resources.getCaption("invName")),
    DISP_DN ("disp", Resources.getCaption("dispName")),
    REPAIR_DN ("repair", Resources.getCaption("repairName")),
    MANAGE_DN ("manage", Resources.getCaption("manageName"));
    String txt;
    String descr;
    ModuleEnum(String t, String d){
        txt = t;
        descr = d;
    }
    public String getTxt(){
        return "dc="+txt+",";
    }
    public String getModule(){
        return txt;
    }
    public String getDescr(){
        return descr;
    }
}
