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
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Created by arkan on 15.08.2016.
 */
@Named
@SessionScoped
public class UserGroupBean implements Serializable{
    public void doGetOut(Group group1, int appNom) throws Exception{
        Objects.requireNonNull(group1);
        Objects.requireNonNull(userManagerBean.getCurrentUser());
        String jmxConnStr = null;
        switch (appNom){
            case 0 : jmxConnStr = "java:global/federation/cu"; break;
            case 1 : jmxConnStr = "java:global/federation/disp"; break;
            case 5 : jmxConnStr = "java:global/federation/inv"; break;
            case 6 : jmxConnStr = "java:global/federation/repair"; break;
        }
        if (jmxConnStr==null) throw new Exception("No active application!");
        InitialContext initialContext = null;
        InitialDirContext initalDirContext = null;
        LdapContext ldapContext = null;
        try {
            initialContext = new InitialContext();
            initalDirContext = (InitialDirContext) initialContext.lookup(jmxConnStr);
            ldapContext = new InitialLdapContext(initalDirContext.getEnvironment(), null);
            LdapJndiWriter ldapWriter = new LdapJndiWriter(ldapContext);
            ldapWriter.removeUserFromGroup(userManagerBean.getCurrentUser(), group1);
        } catch (NamingException e) {
            e.printStackTrace();
        }finally{
            if (ldapContext!=null) ldapContext.close();
            if (initalDirContext!=null) initalDirContext.close();
            if (initialContext!=null) initialContext.close();
        }
        for (Iterator<GroupMembership> itt = groupMemberShip.iterator(); itt.hasNext();){
            GroupMembership groupMembershipItem = itt.next();
            if (groupMembershipItem.getGroup()==group1) itt.remove();
        }
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



    private List<GroupMembership> groupMemberShip=new ArrayList<>();

    public String loadMemberShip(){
        Objects.requireNonNull(userManagerBean.getCurrentUser());
        User currentUser = userManagerBean.getCurrentUser();
        RelationshipManager relationshipManager = authorizationManager.getRelationshipManager();
        RelationshipQuery<GroupMembership> relationshipQuery = relationshipManager.createRelationshipQuery(GroupMembership.class);
        relationshipQuery.setParameter(GroupMembership.MEMBER , currentUser);
        groupMemberShip.clear();
        groupMemberShip.addAll(relationshipQuery.getResultList());
        return "usergroup.xhtml?faces-redirect=true";
    }
}