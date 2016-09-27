package ru.chipn.usermanage.idm;

import org.picketlink.idm.model.basic.Group;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;
import ru.chipn.usermanage.login.ModuleEnum;

import javax.enterprise.context.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.chipn.usermanage.login.ModuleEnum.*;

/**
 * Created by arkan on 02.08.2016.
 */
@Named
@SessionScoped
public class GroupBean implements Serializable{


    public void changeAppListener(ValueChangeEvent event){
        appFg=null;
        appTg=null;
        jmxConnStr = null;
        selectedFgroup = null;
        if (selectedTGroupList ==null) selectedTGroupList =new ArrayList<>();
        else selectedTGroupList.clear();
        if (event.getNewValue()==null || ((String) event.getNewValue()).length()<1) return;
        String app = (String) event.getNewValue();
        ModuleEnum selectedModule = null;
        for (ModuleEnum module: values()){
            if (module.getModule().equals(app)) selectedModule = module;
            if (selectedModule!=null) break;
        }
        if (selectedModule==null) return;

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
                            .stream().map(membership->membership.getGroup())
                            .collect(Collectors.toSet());
                    return membershipGroup.contains(group);
                }
        );
    }
    public String getNameSelectedGroup(){
        if (groupId==null) return null;
        String nameGroup = this.findName(CU_DN);
        if (nameGroup==null) nameGroup=this.findName(INV_DN);
        if (nameGroup==null) nameGroup=this.findName(DISP_DN);
        if (nameGroup==null) nameGroup=this.findName(REPAIR_DN);
        return nameGroup;
    }
    private String findName(ModuleEnum moduleEnum){
        List<SelectItem> listToFind = appBean.getModuleFgOptions().get(moduleEnum);
        if (listToFind==null) appBean.getModuleTgOptions().get(moduleEnum);
        if (listToFind==null) return null;
        final SelectItem selectedItem = listToFind.stream()
                .filter(si->si.getValue().equals(groupId)).findFirst().get();
        return selectedItem!=null ? selectedItem.getLabel(): null;
    }
    /*
    public Group getSelectedGroup(){
        if (groupId==null) return null;
        Group selectedGroup = null;
        selectedGroup = appBean.getGroupCuListFg().stream().filter(gr->gr.getId().equals(groupId)).findFirst().get();
        if (selectedGroup!=null) return selectedGroup;
        selectedGroup = appBean.getGroupInvList().stream().filter(gr->gr.getId().equals(groupId)).findFirst().get();
        if (selectedGroup!=null) return selectedGroup;
        selectedGroup = appBean.getGroupDispList().stream().filter(gr->gr.getId().equals(groupId)).findFirst().get();
        if (selectedGroup!=null) return selectedGroup;
        selectedGroup = appBean.getGroupRepairList().stream().filter(gr->gr.getId().equals(groupId)).findFirst().get();
        if (selectedGroup!=null) return selectedGroup;
        selectedGroup = appBean.getGroupCuListTg().stream().filter(gr->gr.getId().equals(groupId)).findFirst().get();
        return selectedGroup;
    }
    */
    public List<Group> getAppFg(){
        return appFg;
    }
    public List<Group> getAppTg(){
        return appTg;
    }
    public String getJmxConnStr(){
        return jmxConnStr;
    }
    public String toHome(){
        return "home.xhtml?faces-redirect=true";
    }
    public String toUsers(){
        return "users.xhtml?faces-redrect=true";
    }
    public String getGroupId(){
        return groupId;
    }
    public void setGroupId(String groupId1){
        groupId=groupId1;
    }
    public Boolean getCollapsed(){
        return collapsed;
    }
    public void setCollapsed(Boolean collapsed) {
        this.collapsed = collapsed;
    }
    public String getTxt() {return "Управление пользователями!";}
    public void onCollapse(ToggleEvent event) {
        this.collapsed= event.getVisibility().equals(Visibility.VISIBLE);
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
    private String jmxConnStr;
    private List<Group> selectedTGroupList =new ArrayList<>();
    private Group selectedFgroup;
    private String selectedApp;
    private Boolean collapsed = false;
    private String groupId;
    private List<Group> appFg;
    private List<Group> appTg;
    @Inject
    private AppBean appBean;
    @Inject
    private UserGroupBean userGroupBean;
}
