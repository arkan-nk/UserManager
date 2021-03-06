package ru.chipn.usermanage.idm;

import org.picketlink.idm.model.basic.Group;
import org.picketlink.idm.model.basic.GroupMembership;
import ru.chipn.usermanage.login.ModuleEnum;

import javax.enterprise.context.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Managed Bean для работы с LDAP группами для выбранного приложения
 */
@Named
@SessionScoped
public class GroupBean implements Serializable{
    /**
     * JNDI строка соединения заполняется
     * с помощью ModuleEnum из properties из web.xml
     * {@link ModuleEnum}
     */
    private String jmxConnStr;
    /**
     * Выбранные территориальные группы для выбранного приложения
     */
    private List<Group> selectedTGroupList =new ArrayList<>();
    /**
     * Выбранная функциональная группа для выбранного приложения
     */
    private Group selectedFgroup;
    /**
     * Выбранное приложение используется поле ModuleEnum.txt
     * {@link ModuleEnum}
     */
    private String selectedApp;
    /**
     * Все функциональные группы для выбранного приложения
     */
    private List<Group> appFg;
    /**
     * Все территориальные группы для выбранного приложения
     */
    private List<Group> appTg;
    @Inject
    private AppBean appBean;
    @Inject
    private UserGroupBean userGroupBean;

    /**
     * отмена вы
     */
    public void clearSelected(){
        selectedTGroupList.clear();
        selectedFgroup=null;
        selectedApp=null;
        appFg=null; //exactly set null do not clear() !
        appTg=null; //exactly set null do not clear() !
    }

    /**
     * Выбор приложения, переключение в интерфейсе между
     * приложениями АСКУПЭ-2.0
     * @param event
     */
    public void changeAppListener(ValueChangeEvent event){
        appFg=null;
        appTg=null;
        jmxConnStr = null;
        selectedFgroup = null;
        if (selectedTGroupList ==null) selectedTGroupList =new ArrayList<>();
        else selectedTGroupList.clear();
        if (event.getNewValue()==null || ((String) event.getNewValue()).length()<1) return;
        String app = (String) event.getNewValue();
        ModuleEnum selectedModule = Arrays.stream(ModuleEnum.values()).filter(module->module.getModule().equals(app)).findFirst().orElse(null);
        if (selectedModule==null) return;
        this.changeApp(selectedModule);
    }
    private void changeApp(ModuleEnum selectedModule){
        jmxConnStr = selectedModule.getJmxStr();
        switch (selectedModule){
            case INV_DN:{
                appFg = appBean.getGroupInvList();
                break;
            }
            case DISP_DN:{
                appFg = appBean.getGroupDispList();
                break;
            }
            case REPAIR_DN:{
                appFg = appBean.getGroupRepairList();
                break;
            }
            default:{ //CU_DN
                appFg = appBean.getGroupCuListFg();
                appTg = appBean.getGroupCuListTg();
            }
        }
        this.filterMembershipGroup(appFg);
        if (this.appTg!=null && !this.appTg.isEmpty()) this.filterMembershipGroup(appTg);
    }
    private void filterMembershipGroup(List<Group> groupList){
        groupList.removeIf(
                group -> {
                    Set<Group> membershipGroup = userGroupBean.getUserMemberShip()
                            .stream().map(GroupMembership::getGroup)
                            .collect(Collectors.toSet());
                    return membershipGroup.contains(group);
                }
        );
    }

    public List<Group> getAppFg(){
        return appFg;
    }
    public List<Group> getAppTg(){
        return appTg;
    }
    public String getJmxConnStr(){
        return jmxConnStr;
    }

    public String getSelectedApp(){
        return selectedApp;
    }
    public void setSelectedApp(String selectedApp1){
        selectedApp=selectedApp1;
    }
    public List<Group> getSelectedTGroupList(){
        return selectedTGroupList;
    }
    public void setSelectedTGroupList(List<Group> listGroup){
        selectedTGroupList =listGroup;
    }
    public GroupBean(){
        appFg = new ArrayList<>();
        appTg = new ArrayList<>();
    }
    public Group getSelectedFgroup(){
        return selectedFgroup;
    }
    public void setSelectedFgroup(Group group){
        selectedFgroup = group;
    }
    public String getTxt() {return "Управление пользователями!";}
}
