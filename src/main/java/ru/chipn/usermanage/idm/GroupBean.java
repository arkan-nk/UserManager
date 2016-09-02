package ru.chipn.usermanage.idm;

import org.picketlink.idm.model.basic.Group;
import org.picketlink.idm.model.basic.User;
import org.picketlink.idm.query.AttributeParameter;
import org.picketlink.idm.query.IdentityQuery;
import org.picketlink.idm.query.IdentityQueryBuilder;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;
import ru.chipn.usermanage.login.AuthorizationManager;
import ru.chipn.usermanage.login.ModuleEnum;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static org.picketlink.common.constants.LDAPConstants.GROUP_OF_UNIQUE_NAMES;
import static org.picketlink.common.constants.LDAPConstants.OBJECT_CLASS;

/**
 * Created by arkan on 02.08.2016.
 */
@Named
@SessionScoped
public class GroupBean implements Serializable{
    public void onCollapse(ToggleEvent event) {
        this.collapsed= event.getVisibility().equals(Visibility.VISIBLE);
    }
    public void doGetOut(Group group1, User currentUser, final int appNom) throws Exception{
        Objects.requireNonNull(group1);
        Objects.requireNonNull(currentUser);
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
            ldapWriter.removeUserFromGroup(currentUser, group1);
        } catch (NamingException e) {
            facesContext.addMessage(null, new FacesMessage(e.getExplanation()));
        }finally{
            if (ldapContext!=null) ldapContext.close();
            if (initalDirContext!=null) initalDirContext.close();
            if (initialContext!=null) initialContext.close();
        }
    }

    @PostConstruct
    public void init(){
        groupCuList = (List<Group>) getListGroups(ModuleEnum.CU_DN);
        groupInvList = (List<Group>)getListGroups(ModuleEnum.INV_DN);
        groupDispList = (List<Group>)getListGroups(ModuleEnum.DISP_DN);
        groupRepairList = (List<Group>)getListGroups(ModuleEnum.REPAIR_DN);
    }
    private List<Group> getListGroups(ModuleEnum moduleEnum){
        IdentityQueryBuilder iqb = authorizationManager.getIdentityManager().getQueryBuilder();
        AttributeParameter objectClassParameter = Group.QUERY_ATTRIBUTE.byName(OBJECT_CLASS);
        AttributeParameter oParameter = Group.QUERY_ATTRIBUTE.byName(LDAPATTRS.ORGANIZATIONNAME.getTxt());
        IdentityQuery<Group> query = iqb.createIdentityQuery(Group.class);
        List<Group> group = query
                .where(
                    iqb.equal(objectClassParameter, GROUP_OF_UNIQUE_NAMES),
                    iqb.equal(oParameter, moduleEnum.getModule())
                )
                .getResultList();
        return group;
    }
    public List<Group> getGroupCuList() {
        return groupCuList;
    }
    public List<Group> getGroupInvList() {
        return groupInvList;
    }
    public List<Group> getGroupDispList() {
        return groupDispList;
    }
    public List<Group> getGroupRepairList() {
        return groupRepairList;
    }
    public String toHome(){
        return "home.xhtml?faces-redirect=true";
    }
    public String toUsers(){
        return "users.xhtml?faces-redrect=true";
    }
    public Boolean getCollapsed(){
        return collapsed;
    }
    public void setCollapsed(Boolean collapsed) {
        this.collapsed = collapsed;
    }
    public String getTxt() {return "Управление пользователями!";}
    private List<Group> groupCuList;
    private List<Group> groupInvList;
    private List<Group> groupDispList;
    private List<Group> groupRepairList;
    @Inject
    private FacesContext facesContext;
    @Inject
    private AuthorizationManager authorizationManager;
    private Boolean collapsed = false;
}
