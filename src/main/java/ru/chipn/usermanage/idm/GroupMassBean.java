package ru.chipn.usermanage.idm;

import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.model.basic.Group;
import org.picketlink.idm.model.basic.GroupMembership;
import org.picketlink.idm.model.basic.User;
import org.picketlink.idm.query.RelationshipQuery;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.event.UnselectEvent;
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
 * ManagedBean операции для выбранной группы с пользователями.
 */
@Named
@ViewScoped
public class GroupMassBean implements Serializable {
    /**
     * Отфильтрованные группы с типом результата фильтрации - множество
     * используется Framework Primefaces для фильтрации в UI
     */
    private List<SelectItem> filteredGroup;
    /**
     * Отфильтрованная группа с типом результата фильтрации - одна запись
     * используется Framework Primefaces для фильтрации в UI
     */
    private SelectItem selectedSelItemGroup;
    /**
     * Выбранная группа идентификатор
     */
    private String groupId;
    /**
     * Выбранная группа объект
     */
    private Group selectedG;
    /**
     * используется в UI для скрытия боковой панели
     */
    private Boolean collapsed = false;
    @Inject
    private AppBean appBean;
    @Inject
    private AuthorizationManager authorizationManager;
    @Inject
    private UserManagerBean userManagerBean;
    /**
     * Выбранное приложение
     */
    private ModuleEnum selectedModuleEnum;
    /**
     * Пользователи являющиеся участниками выбранной группы
     */
    private List<User> members = new ArrayList<>();
    /**
     * Все пользователи LDAP не являющиеся участниками выбранной группы
     */
    private List<User> allUsers = new ArrayList<>();
    /**
     * Пользователи "кандидаты" на включение(исключение) в состав (из состава)
     * выбранной группы
     */
    private List<User> candidates= new ArrayList<>();

    /**
     * включение пользователей в состав выбранной группы 2 шаг
     */
    public void grantStep2(){
        members.addAll(candidates);
        allUsers.removeIf(u->candidates.contains(u));
        candidates.clear();
    }

    /**
     * исключение пользователей из состава выбранной группы шаг 2
     */
    public void massRevokeStep2(){
        allUsers.addAll(candidates);
        members.removeIf(memb->candidates.contains(memb));
        candidates.clear();
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
    public void setGroupId(final String groupId1){
        groupId = groupId1;
    }

    public String getNameSelectedGroup(){
        this.clear();
        selectedG = null;
        if (groupId==null || groupId.length()<1) return null;
        findSelectedGroup();
        String result = null;
        if (selectedG !=null) {
            this.loadMembersUser();
            result = selectedG.getAttribute("description").getValue() + " (" + selectedG.getName() + ")";
            allUsers = userManagerBean.getUsers().stream().filter(user -> !members.contains(user)).collect(Collectors.toList());
        }
        return result;
    }

    private void clear(){
        allUsers.clear();
        members.clear();
        candidates.clear();
    }

    private void loadMembersUser() {
        final RelationshipManager relationshipManager = authorizationManager.getRelationshipManager();
        RelationshipQuery<GroupMembership> relationshipQuery = relationshipManager.createRelationshipQuery(GroupMembership.class);
        relationshipQuery.setParameter(GroupMembership.GROUP , selectedG);
        final List<GroupMembership> groupMemberShipList = relationshipQuery.getResultList();
        final List<User> userList = groupMemberShipList.stream().map(gm->(User) gm.getMember())
                .filter(user->!(user.getLoginName().equalsIgnoreCase("nobody") || user.getLoginName().equals("quartzjob@ncserv.ru")))
                .collect(Collectors.toList());
        members.addAll(userList);
    }

    private void findSelectedGroup(){
        selectedG = appBean.getGroupCuListFg().stream().filter(gr->gr.getId().equals(groupId)).findFirst().orElse(null);
        if (selectedG !=null) {
            selectedModuleEnum = ModuleEnum.CU_DN;
            return;
        }
        selectedG = appBean.getGroupInvList().stream().filter(gr->gr.getId().equals(groupId)).findFirst().orElse(null);
        if (selectedG !=null) {
            selectedModuleEnum = ModuleEnum.INV_DN;
            return;
        }
        selectedG = appBean.getGroupDispList().stream().filter(gr->gr.getId().equals(groupId)).findFirst().orElse(null);
        if (selectedG !=null) {
            selectedModuleEnum = ModuleEnum.DISP_DN;
            return;
        }
        selectedG = appBean.getGroupRepairList().stream().filter(gr->gr.getId().equals(groupId)).findFirst().orElse(null);
        if (selectedG !=null) {
            selectedModuleEnum = ModuleEnum.REPAIR_DN;
            return;
        }
        selectedG = appBean.getGroupCuListTg().stream().filter(gr->gr.getId().equals(groupId)).findFirst().orElse(null);
        if (selectedG != null) selectedModuleEnum = ModuleEnum.CU_DN;
    }

    public List<User> getMembers(){
        return members;
    }
    public void setMembers(final List<User> ll){
        members = ll;
    }
    public String getJmxConn(){
        return selectedModuleEnum!=null ? selectedModuleEnum.getJmxStr() : null;
    }

    public Group getSelectedG(){
        return selectedG;
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
    public List<SelectItem> getFilteredGroup() {
        return filteredGroup;
    }
    public void setFilteredGroup(final List<SelectItem> filteredGroup) {
        this.filteredGroup = filteredGroup;
    }
    public SelectItem getSelectedSelItemGroup() {
        return selectedSelItemGroup;
    }
    public void setSelectedSelItemGroup(final SelectItem selectedSelItemGroup) {
        this.selectedSelItemGroup = selectedSelItemGroup;
    }
    public void onRowSelect(SelectEvent event) {
        if (this.selectedSelItemGroup!=null) groupId = (String) this.selectedSelItemGroup.getValue();
    }

    public void onRowUnselect(UnselectEvent event) {
        this.clear();
    }

}
