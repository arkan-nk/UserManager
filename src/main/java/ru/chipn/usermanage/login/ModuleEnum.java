package ru.chipn.usermanage.login;

/**
 * Created by arkan on 28.06.2016.
 */
public enum ModuleEnum {
    CU_DN ("cu", Resources.getCaption("cuName"), Resources.getParam("connJMXCu")),
    INV_DN ("inv", Resources.getCaption("invName"), Resources.getParam("connJMXInv")),
    DISP_DN ("disp", Resources.getCaption("dispName"), Resources.getParam("connJMXDisp")),
    REPAIR_DN ("repair", Resources.getCaption("repairName"), Resources.getParam("connJMXRepair")),
    MANAGE_DN ("manage", Resources.getCaption("manageName"), null);
    String txt;
    String descr;
    String jmxStr;
    ModuleEnum(String t, String d, String conn){
        txt = t;
        descr = d;
        jmxStr = conn;
    }
    public String getModule(){
        return txt;
    }
    public String getDescr(){
        return descr;
    }
    public String getJmxStr(){
        return jmxStr;
    }
}
