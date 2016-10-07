package ru.chipn.usermanage.idm;

import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.model.basic.Group;
import org.picketlink.idm.model.basic.GroupMembership;
import org.picketlink.idm.model.basic.User;
import org.picketlink.idm.query.RelationshipQuery;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;
import ru.chipn.usermanage.login.AuthorizationManager;
import ru.chipn.usermanage.login.ModuleEnum;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by arkan on 04.10.2016.
 */
@Named
@ViewScoped
public class GroupMassBean implements Serializable {
    public void grantStep2(){
        members.addAll(candidates);
        allUsers.removeIf(u->candidates.contains(u));
        candidates.clear();
    }
    public void massRevokeStep2(){
        allUsers.addAll(selection);
        members.removeIf(memb->selection.contains(memb));
        selection.clear();
        //listRevoke.clear();
    }
    public Boolean getCollapsed(){
        return collapsed;
    }
    public void setCollapsed(Boolean collapsed) {
        this.collapsed = collapsed;
    }
    public void onCollapse(ToggleEvent event) {
        this.collapsed= event.getVisibility().equals(Visibility.VISIBLE);
    }
    public String getGroupId(){
        return groupId;
    }
    public void setGroupId(String groupId1){
        groupId = groupId1;
    }

    public String getNameSelectedGroup(){
        this.clear();
        selectedFG = null;
        if (groupId==null || groupId.length()<1) return null;
        findSelectedGroup();
        String result = null;
        if (selectedFG!=null) {
            this.loadMembersUser();
            result = selectedFG.getName();
            allUsers = userManagerBean.getUsers().stream().filter(user -> !members.contains(user)).collect(Collectors.toList());
        }
        return result;
    }
    private void clear(){
        allUsers.clear();
        members.clear();
        selection.clear();
        candidates.clear();
        //listRevoke.clear();
    }

    private void loadMembersUser() {
        RelationshipManager relationshipManager = authorizationManager.getRelationshipManager();
        RelationshipQuery<GroupMembership> relationshipQuery = relationshipManager.createRelationshipQuery(GroupMembership.class);
        relationshipQuery.setParameter(GroupMembership.GROUP , selectedFG);
        List<GroupMembership> groupMemberShipList = relationshipQuery.getResultList();
        List<User> userList = groupMemberShipList.stream().map(gm->(User) gm.getMember())
                .filter(user->!(user.getLoginName().equalsIgnoreCase("nobody") || user.getLoginName().equals("quartzjob@ncserv.ru")))
                .collect(Collectors.toList());
        members.addAll(userList);
    }

    private void findSelectedGroup(){
        selectedFG = appBean.getGroupCuListFg().stream().filter(gr->gr.getId().equals(groupId)).findFirst().orElse(null);
        if (selectedFG!=null) {
            selectedModuleEnum = ModuleEnum.CU_DN;
            return;
        }
        selectedFG = appBean.getGroupInvList().stream().filter(gr->gr.getId().equals(groupId)).findFirst().orElse(null);
        if (selectedFG!=null) {
            selectedModuleEnum = ModuleEnum.INV_DN;
            return;
        }
        selectedFG = appBean.getGroupDispList().stream().filter(gr->gr.getId().equals(groupId)).findFirst().orElse(null);
        if (selectedFG!=null) {
            selectedModuleEnum = ModuleEnum.DISP_DN;
            return;
        }
        selectedFG = appBean.getGroupRepairList().stream().filter(gr->gr.getId().equals(groupId)).findFirst().orElse(null);
        if (selectedFG!=null) {
            selectedModuleEnum = ModuleEnum.REPAIR_DN;
            return;
        }
    }
    private String findName(ModuleEnum moduleEnum){
        List<SelectItem> listToFind = appBean.getModuleFgOptions().get(moduleEnum);
        if (listToFind==null) appBean.getModuleTgOptions().get(moduleEnum);
        if (listToFind==null) return null;
        final SelectItem selectedItem = listToFind.stream()
                .filter(si->si.getValue().equals(groupId)).findFirst().orElse(null);
        return selectedItem!=null ? selectedItem.getLabel(): null;
    }
    public List<User> getMembers(){
        return members;
    }
    public void setMembers(List<User> ll){
        members = ll;
    }
    public Integer getCount(){
        return count;
    }
    public void setCount(Integer c){
        count=c;
    }
    public String getJmxConn(){
        return selectedModuleEnum!=null ? selectedModuleEnum.getJmxStr() : null;
    }
    /*
    public void loadListRevoke(){
        listRevoke.clear();
        listRevoke.addAll(selection);
    }
    public List<User> getListRevoke(){
        return listRevoke;
    }
    */
    public Group getSelectedFG(){
        return selectedFG;
    }
    public List<User> getSelection(){
        return selection;
    }
    public void setSelection(List<User> li){
        selection = li;
    }
    public List<User> getAllUsers(){
        return allUsers;
    }
    public void setAllUsers(List<User> allUsers1){
        allUsers = allUsers1;
    }
    public List<User> getCandidates(){
        return candidates;
    }
    public void setCandidates(List<User> cc){
        candidates = cc;
    }
    private ModuleEnum selectedModuleEnum;
    private List<User> selection= new ArrayList<>();
    private List<User> members = new ArrayList<>();
    private List<User> allUsers = new ArrayList<>();
    private List<User> candidates= new ArrayList<>();
    //private List<User> listRevoke= new ArrayList<>();

    private String groupId;
    private Group selectedFG;
    private Boolean collapsed = false;
    private Integer count=10;
    @Inject
    private AppBean appBean;
    @Inject
    private AuthorizationManager authorizationManager;
    @Inject
    private UserManagerBean userManagerBean;
}
