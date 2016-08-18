package ru.chipn.usermanage.idm;

import org.picketlink.idm.model.Attribute;
import org.picketlink.idm.model.basic.Group;
import org.picketlink.idm.model.basic.GroupMembership;
import org.picketlink.idm.model.basic.User;
import org.picketlink.idm.query.RelationshipQuery;
import ru.chipn.usermanage.login.AuthorizationManager;
import ru.chipn.usermanage.login.ModuleEnum;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ru.chipn.usermanage.login.ConfigurationEnum.*;

/**
 * Created by arkan on 15.08.2016.
 */
@Named
@RequestScoped
public class UserGroupBean implements Serializable {
    public void doGetOut(Group group1){
        Objects.requireNonNull(group1);
        Objects.requireNonNull(authorizationManager.getRelationshipManager());
        Objects.requireNonNull(userManagerBean.getCurrentUser());
        //BasicModel.removeFromGroup(authorizationManager.getRelationshipManager(),userManagerBean.getCurrentUser(), group1);
        /*
        RelationshipQuery<GroupMembership> query = authorizationManager.getRelationshipManager().createRelationshipQuery(GroupMembership.class);
        query.setParameter(GroupMembership.MEMBER, userManagerBean.getCurrentUser());
        query.setParameter(GroupMembership.GROUP, group1);
        for (GroupMembership membership : query.getResultList()) {

            System.out.println(membership.getGroup().getPath());
            System.out.println(membership.getGroup().getPartition().getName());
            System.out.println(membership.getMember().getPartition().getName());
            membership.getAttributes().forEach(attr->{
                System.out.println(attr.getName());
                System.out.println(attr.getValue());
            });

            authorizationManager.getRelationshipManager().remove(membership);
        }
        */
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
        final String oustr = GROUPS_OU.getTxt()+moduleEnum.getTxt()+BASE_DN.getTxt()+ROOT_DN.getTxt();
        List<Group> list = new ArrayList<>();
        groupMemberShip.stream().filter(
             groupMemb ->
                 groupMemb.getGroup()
                         .getAttribute(LDAPATTRS.ORGANIZATIONNAME.getTxt())
                         .getValue().equals(moduleEnum.getModule())
        ).forEach(groupMemb ->list.add(groupMemb.getGroup()));
        return list;
    }
    @Inject
    private AuthorizationManager authorizationManager;
    @Inject
    private UserManagerBean userManagerBean;
    private List<GroupMembership> groupMemberShip;

    @PostConstruct
    public void init(){
        Objects.requireNonNull(userManagerBean.getCurrentUser());
        User currentUser = userManagerBean.getCurrentUser();
        RelationshipQuery<GroupMembership> relationshipQuery = authorizationManager.getRelationshipManager().createRelationshipQuery(GroupMembership.class);
        relationshipQuery.setParameter(GroupMembership.MEMBER, currentUser);
        groupMemberShip = relationshipQuery.getResultList();
    }
}
