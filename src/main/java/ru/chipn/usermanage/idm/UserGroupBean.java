package ru.chipn.usermanage.idm;

import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.model.basic.Group;
import org.picketlink.idm.model.basic.GroupMembership;
import org.picketlink.idm.model.basic.User;
import org.picketlink.idm.query.RelationshipQuery;
import ru.chipn.usermanage.login.AuthorizationManager;
import ru.chipn.usermanage.login.ModuleEnum;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

/**
 * ManagedBean операции для выбранного пользователя с группами
 */
@Named
@SessionScoped
public class UserGroupBean implements Serializable{

    @Inject
    private AuthorizationManager authorizationManager;
    @Inject
    private UserManagerBean userManagerBean;
    /**
     * Выбранное приложение
     */
    private ModuleEnum currentModule;
    /**
     * Участие пользователя в группах
     */
    private List<GroupMembership> userMemberShip = new ArrayList<>();

    public void dropMembershipUser(final Group group1){
        userMemberShip.removeIf(groupMembershipItem -> groupMembershipItem.getGroup().getId().equals(group1.getId()));
    }
    public List<GroupMembership> getUserMemberShip(){
        return userMemberShip;
    }
    public List<Group> getListInvGroup(){
        return this.getListModuleGroup(ModuleEnum.INV_DN);
    }
    public List<Group> getListCuGroup(){
        return this.getListModuleGroup(ModuleEnum.CU_DN);
    }
    public List<Group> getListDispGroup(){
        return this.getListModuleGroup(ModuleEnum.DISP_DN);
    }
    public List<Group> getListRepairGroup(){
        return this.getListModuleGroup(ModuleEnum.REPAIR_DN);
    }
    private List<Group> getListModuleGroup(ModuleEnum moduleEnum){
        List<Group> list = new ArrayList<>();
        userMemberShip.stream().filter(
             groupMemb ->
                 groupMemb.getGroup()
                         .getAttribute(LDAPATTRS.ORGANIZATIONNAME.getTxt())
                         .getValue().equals(moduleEnum.getModule())
        ).forEach(groupMemb ->list.add(groupMemb.getGroup()));
        return list;
    }
    public String getJmxConn(){
        return currentModule!=null ? currentModule.getJmxStr() : null;
    }
    public Boolean isNoMemberShip(){
        return userMemberShip.isEmpty();
    }
    public void setCurrentModule(final String moduleT){
        this.currentModule = Arrays.stream(ModuleEnum.values()).filter(me->me.getModule().equals(moduleT)).findFirst().orElse(null);
    }

    public String loadMemberShipGo(){
        loadMemberShip();
        return "usergroup.xhtml?faces-redirect=true";
    }

    /**
     * Загрузка членства в группах для выбранного пользователя
     */
    public void loadMemberShip(){
        Objects.requireNonNull(userManagerBean.getCurrentUser());
        final User currentUser = userManagerBean.getCurrentUser();
        final RelationshipManager relationshipManager = authorizationManager.getRelationshipManager();
        final RelationshipQuery<GroupMembership> relationshipQuery = relationshipManager.createRelationshipQuery(GroupMembership.class);
        relationshipQuery.setParameter(GroupMembership.MEMBER , currentUser);
        userMemberShip.clear();
        userMemberShip.addAll(relationshipQuery.getResultList());
    }
}
